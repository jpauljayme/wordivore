package dev.jp.wordivore.service;

import dev.jp.wordivore.dto.AuthorDto;
import dev.jp.wordivore.dto.LibraryItemDto;
import dev.jp.wordivore.dto.OpenLibraryDto;
import dev.jp.wordivore.dto.WorkResponseDto;
import dev.jp.wordivore.exception.OpenLibraryWorkNotFoundException;
import dev.jp.wordivore.model.*;
import dev.jp.wordivore.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class LibraryItemService {
    private final EditionRepository editionRepository;
    private final AppUserRepository appUserRepository;
    private final LibraryItemRepository libraryItemRepository;
    private final WorkRepository workRepository;
    private final WorkAuthorRepository workAuthorRepository;
    private final PersonRepository personRepository;
    private final EditionContributorRepository editionContributorRepository;
    private final OpenLibraryService openLibraryService;
    private final S3Service s3Service;


    public List<LibraryItem> getUserLibrary(Long id) {
        return libraryItemRepository.findAllByAppUser_Id(id);
    }

    public List<LibraryItem> getUserLibraryMostRecent(Long id) {
        return libraryItemRepository.findTop3ByAppUser_IdOrderByCreatedAtDesc(id);
    }


    public void insertBook(OpenLibraryDto openLibraryDto, String isbn, Long userId) throws OpenLibraryWorkNotFoundException {

        Edition edition = (isbn != null && isbn.length() == 10) ?
                editionRepository.findByIsbn10(isbn).orElse(null)
                : editionRepository.findByIsbn13(isbn).orElse(null);

        //If edition exists, just attach to library item for user and then exit if not yet added.
        if (edition != null) {

            if (!libraryItemRepository.existsByAppUser_IdAndEdition_Id(userId, edition.id)) {
                LibraryItem newLibraryItem = LibraryItem.builder()
                        .appUser(appUserRepository.getReferenceById(userId))
                        .status(ShelfStatus.TO_READ)
                        .edition(edition)
                        .build();
                libraryItemRepository.save(newLibraryItem);
            }

            return;
        }


        //Since this is a new edition, we first check if there is an existing Work thru work key.
        Work work = workRepository.findByKey(openLibraryDto.key())
                .orElseGet(() -> {

                    //Let's create a new work via OL Work Api
                    WorkResponseDto workResponseDto = openLibraryService.findWorkByKey(openLibraryDto.key());

                    if (workResponseDto != null && workResponseDto.key() != null && !workResponseDto.key().isEmpty()) {
                        String description = workResponseDto.description() != null ? workResponseDto.description() : "";
                        String firstSentence = workResponseDto.firstSentence() != null ? workResponseDto.firstSentence().value() : "";
                        List<String> subjects = workResponseDto.subjects() != null ? workResponseDto.subjects() : Collections.<String>emptyList();

                        Work workToSave = Work.builder()
                                .key(workResponseDto.key())
                                .title(workResponseDto.title())
                                .description(description)
                                .firstSentence(firstSentence)
                                .subjects(subjects)
                                .build();

                        return workRepository.save(workToSave);
                    }
                    return null;
                });

        if (work == null) throw new OpenLibraryWorkNotFoundException();

        for (String name : openLibraryDto.authors()) {
            Person p = personRepository.findByName(name)
                    .orElseGet(() -> personRepository.save(Person.builder()
                            .name(name)
                            .build()));
            if (!workAuthorRepository.existsByWork_IdAndPerson_Id(work.id, p.id)) {
                WorkAuthor workAuthor = WorkAuthor.builder()
                        .person(p)
                        .work(work)
                        .build();
                workAuthorRepository.save(workAuthor);
            }
        }


        String isbn10 = !openLibraryDto.isbn10().isEmpty() ? openLibraryDto.isbn10().getFirst() : "";
        String isbn13 = !openLibraryDto.isbn13().isEmpty() ? openLibraryDto.isbn13().getFirst() : "";
        Edition newEdition = Edition.builder()
                .title(openLibraryDto.title())
                .pages(openLibraryDto.pages())
                .byStatement("")
                .publicationDate(openLibraryDto.publicationDate())
                .isbn10(isbn10)
                .isbn13(isbn13)
                .editionName(openLibraryDto.editionName())
                .publishers(openLibraryDto.publishers())
                .coverUrl(openLibraryDto.coverUrl())
                .coverKey(openLibraryDto.coverKey())
                .work(work)
                .build();

        editionRepository.save(newEdition);

        LibraryItem newLibraryItem = LibraryItem.builder()
                .edition(newEdition)
                .appUser(appUserRepository.getReferenceById(userId))
                .status(ShelfStatus.TO_READ)
                .build();

        libraryItemRepository.save(newLibraryItem);
    }

    public List<LibraryItemDto> getUserLibraryCurrentReads(Long userId) {

        List<LibraryItem> libraryItems = libraryItemRepository.findAllByAppUser_IdAndStatus(userId, ShelfStatus.CURRENTLY_READING);

        //Get work authors. By batch!
        var workIds = libraryItems.stream().map(li -> li.getEdition().getWork().id).collect(Collectors.toSet());

        //Get edition contributors / translator. Do the same above.
        Map<Long, List<String>> authorsByWork = workAuthorRepository.findRowsByWork_Id(workIds).stream()
                .collect(Collectors.groupingBy(
                        AuthorDto::workId,
                        LinkedHashMap::new,
                        Collectors.mapping(AuthorDto::name, Collectors.toList())
                ));

        return libraryItems.stream()
                .map(li -> {

                    var e = li.getEdition();
                    var w = e.getWork();
                    var subjects = w.getSubjects() == null ? List.<String>of() : w.getSubjects();
                    var top4 = subjects.size() <= 4 ? subjects : subjects.subList(0, 4);

                    return new LibraryItemDto(
                            li.id,
                            e.getTitle(),
                            authorsByWork.getOrDefault(w.id, List.of()),
                            top4,
                            e.getCoverKey(),
                            li.getStatus(),
                            e.getEditionName(),
                            e.getPages(),
                            e.getPublicationDate()
                    );
                })
                .toList();
    }

    public List<LibrarySection> getUserLibraryAllSections(Long userId) {
        List<LibraryItem> libraryItems = libraryItemRepository.findAllByAppUser_Id(userId);


        //Get work authors. By batch!
        var workIds = libraryItems.stream().map(li -> li.getEdition().getWork().id).collect(Collectors.toSet());


        //Get edition contributors / translator. Do the same above.
        Map<Long, List<String>> authorsByWork = workAuthorRepository.findRowsByWork_Id(workIds).stream()
                .collect(Collectors.groupingBy(
                        AuthorDto::workId,
                        LinkedHashMap::new,
                        Collectors.mapping(AuthorDto::name, Collectors.toList())
                ));

        List<LibraryItemDto> cards = libraryItems.stream()
                .map(li -> {

                    var e = li.getEdition();
                    var w = e.getWork();
                    var subjects = w.getSubjects() == null ? List.<String>of() : w.getSubjects();
                    var top4 = subjects.size() <= 4 ? subjects : subjects.subList(0, 4);

                    return new LibraryItemDto(
                            li.id,
                            e.getTitle(),
                            authorsByWork.getOrDefault(w.id, List.of()),
                            top4,
                            e.getCoverKey(),
                            li.getStatus(),
                            e.getEditionName(),
                            e.getPages(),
                            e.getPublicationDate()
                    );
                }).toList();

        EnumMap<ShelfStatus, List<LibraryItemDto>> grouped = new EnumMap<>(ShelfStatus.class);
        for (var c : cards) {
            grouped.computeIfAbsent(c.status(), k -> new ArrayList<>()).add(c);
        }


        return List.of(
                new LibrarySection(
                        ShelfStatus.TO_READ,
                        grouped.getOrDefault(ShelfStatus.TO_READ, new ArrayList<>())
                ),
                new LibrarySection(
                        ShelfStatus.CURRENTLY_READING,
                        grouped.getOrDefault(ShelfStatus.CURRENTLY_READING, new ArrayList<>())
                ),
                new LibrarySection(
                        ShelfStatus.READ,
                        grouped.getOrDefault(ShelfStatus.READ, new ArrayList<>())
                ),
                new LibrarySection(
                        ShelfStatus.DNF,
                        grouped.getOrDefault(ShelfStatus.DNF, new ArrayList<>())
                )
        );
    }
}
