package dev.jp.wordivore.service;

import dev.jp.wordivore.dto.AuthorDto;
import dev.jp.wordivore.dto.LibraryItemDto;
import dev.jp.wordivore.exception.BookNotFoundException;
import dev.jp.wordivore.model.*;
import dev.jp.wordivore.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ShelfReadService {
    private final LibraryItemRepository libraryItemRepository;
    private final WorkAuthorRepository workAuthorRepository;


    @Cacheable(value = "user:shelves",
            key = "#userId",
            sync = true
    )
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
                            e.getPages(),
                            e.getPublicationDate(),
                            li.getReadStart(),
                            li.getReadEnd()
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


    public List<LibraryItemDto> getUserLibraryByStatus(Long userId, ShelfStatus status) {

        List<LibraryItem> libraryItems = libraryItemRepository.findAllByAppUser_IdAndStatus(userId, status);

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
                            e.getPages(),
                            e.getPublicationDate(),
                            li.getReadStart(),
                            li.getReadEnd()
                    );
                })
                .toList();
    }

    public Optional<LibraryItemDto> getBookById(Long id) throws BookNotFoundException {
        LibraryItem li = libraryItemRepository.findWithEditionAndWorkById(id)
                .orElseThrow(BookNotFoundException::new);

        Edition e = li.getEdition();
        Work work = e.getWork();


        //Get edition contributors / translator. Do the same above.
       List<String> authors = workAuthorRepository.findAllByWork_Id(work.id).stream()
               .map(AuthorDto::name)
               .toList();

       var subjects = work.getSubjects() == null ? List.<String>of() : work.getSubjects();
       var top4 = subjects.size() <= 4 ? subjects : subjects.subList(0, 4);

       return Optional.of(
            new LibraryItemDto(
               li.id,
               e.getTitle(),
               authors,
               top4,
               e.getCoverKey(),
               li.getStatus(),
               e.getPages(),
               e.getPublicationDate(),
               li.getReadStart(),
               li.getReadEnd()
       ));
    }
}
