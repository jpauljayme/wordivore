package dev.jp.wordivore.service;

import dev.jp.wordivore.dto.AuthorDto;
import dev.jp.wordivore.dto.LibraryItemDto;
import dev.jp.wordivore.dto.OpenLibraryDto;
import dev.jp.wordivore.dto.WorkResponseDto;
import dev.jp.wordivore.exception.LibraryItemNotFoundException;
import dev.jp.wordivore.exception.OpenLibraryWorkNotFoundException;
import dev.jp.wordivore.model.*;
import dev.jp.wordivore.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ShelfWriteService {
    private final EditionService editionService;
    private final EditionRepository editionRepository;
    private final AppUserRepository appUserRepository;
    private final LibraryItemRepository libraryItemRepository;
    private final WorkRepository workRepository;
    private final WorkAuthorRepository workAuthorRepository;
    private final PersonRepository personRepository;
    private final OpenLibraryService openLibraryService;

    private final CacheManager cacheManager;

    @Caching(evict = {
        //Evict the user's TO_READ shelf list (you add a TO_READ item)
        @CacheEvict(cacheNames = "user:shelves",
            key = "T(String).format('%s:%s', #userId, T(dev.jp.wordivore.model.ShelfStatus).TO_READ.name())"),
        //Evict the user's prebuilt sections
        @CacheEvict(cacheNames = "user:shelves", key = "#userId")
    })
    public void insertBook(OpenLibraryDto openLibraryDto, String isbn, Long userId) throws OpenLibraryWorkNotFoundException {

        Edition edition = (isbn != null && isbn.length() == 10) ?
                editionService.findByIsbn10(isbn).orElse(null)
                : editionService.findByIsbn13(isbn).orElse(null);

        //If edition exists, just attach to library item for user and then exit if not yet added.
        if (edition != null) {

            if (!libraryItemRepository.existsByAppUser_IdAndEdition_Id(userId, edition.id)) {
                LibraryItem newLibraryItem = LibraryItem.builder()
                        .appUser(appUserRepository.getReferenceById(userId))
                        .status(ShelfStatus.TO_READ)
                        .edition(edition)
                        .readStart(LocalDate.now())
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
                        List<String> subjects = workResponseDto.subjects() != null ? workResponseDto.subjects() : Collections.emptyList();

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



        Edition newEdition = Edition.builder()
                .title(openLibraryDto.title())
                .pages(openLibraryDto.pages())
                .byStatement("")
                .publicationDate(openLibraryDto.publicationDate())
                .isbn10(openLibraryDto.isbn10())
                .isbn13(openLibraryDto.isbn13())
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
                .readStart(LocalDate.now())
                .build();

        libraryItemRepository.save(newLibraryItem);
    }

    @CacheEvict(value = "user:shelves", key = "#userId")
    public LibraryItemDto updateShelfStatus(Long userId, Long id, ShelfStatus newStatus) throws LibraryItemNotFoundException {

        LibraryItem libraryItem = libraryItemRepository.findById(id)
                .orElseThrow(LibraryItemNotFoundException::new);


        libraryItem.setStatus(newStatus);
        libraryItemRepository.save(libraryItem);

       LibraryItem updated = libraryItemRepository.findWithEditionAndWorkById(id).get();


        var e = updated.getEdition();
        var w = e.getWork();
        var subjects = w.getSubjects() == null ? List.<String>of() : w.getSubjects();
        var top4 = subjects.size() <= 4 ? subjects : subjects.subList(0, 4);

        List<String> authors = workAuthorRepository
            .findAllByWork_Id(w.id)
            .stream()
            .map( AuthorDto::name)
            .toList();


        return new LibraryItemDto(
            updated.id,
            updated.getEdition().getTitle(),
            authors,
            top4,
            e.getCoverKey(),
            updated.getStatus(),
            e.getPages(),
            e.getPublicationDate(),
            updated.getReadStart(),
            updated.getReadEnd()
        );
    }

}
