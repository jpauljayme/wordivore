package dev.jp.wordivore.service;

import dev.jp.wordivore.dto.OpenLibraryDto;
import dev.jp.wordivore.dto.WorkResponseDto;
import dev.jp.wordivore.exception.BookDuplicateIsbnException;
import dev.jp.wordivore.mapstruct.OpenLibraryEditionMapper;
import dev.jp.wordivore.model.*;
import dev.jp.wordivore.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class LibraryItemService {
    private final EditionRepository editionRepository;
    private final OpenLibraryEditionMapper bookMapper;
    private final AppUserRepository appUserRepository;
    private final LibraryItemRepository libraryItemRepository;
    private final WorkRepository workRepository;
    private final WorkAuthorRepository workAuthorRepository;
    private final PersonRepository personRepository;
    private final EditionContributorRepository editionContributorRepository;
    private final OpenLibraryService openLibraryService;


    public List<LibraryItem> getUserLibrary(Long id) {
//        return bookRepository.findAllByAppUser_Id(id);
        return libraryItemRepository.findAllByAppUser_Id(id);
    }

    public List<LibraryItem> getUserLibraryMostRecent(Long id) {
//        return bookRepository.findTop3ByAppUser_IdOrderByCreatedAtDesc(id);
        return libraryItemRepository.findTop3ByAppUser_IdOrderByCreatedAtDesc(id);
    }


    public void insertBook(OpenLibraryDto openLibraryDto, String isbn, Long userId) throws BookDuplicateIsbnException {

        Edition edition = (isbn != null && isbn.length() == 10) ?
            editionRepository.findByIsbn10(isbn).orElse(null)
            : editionRepository.findByIsbn13(isbn).orElse(null);

        //If edition exists, just attach to library item for user and then exit if not yet added.
        if(edition != null){

             if(!libraryItemRepository.existsByAppUser_IdAndEdition_Id(userId, edition.id)){
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
                .orElseGet( () -> {

                    //Let's create a new work via OL Work Api
                    WorkResponseDto workResponseDto = openLibraryService.findWorkByKey(openLibraryDto.key());

                    Work workToSave = Work.builder()
                            .key(workResponseDto.key())
                            .title(workResponseDto.title())
                            .description(workResponseDto.description())
                            .firstSentence(workResponseDto.firstSentence().value())
                            .subjects(workResponseDto.subjects())
                            .build();

                    return workRepository.save(workToSave);
                });


        for(String name : openLibraryDto.authors()){
            Person p = personRepository.findByName(name)
                    .orElseGet( () -> personRepository.save(Person.builder()
                                    .name(name)
                                    .build()));
            if(!workAuthorRepository.existsByWork_IdAndPerson_Id(work.id, p.id)){
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
                .isbn10(openLibraryDto.isbn10().getFirst())
                .isbn13(openLibraryDto.isbn13().getFirst())
                .editionName(openLibraryDto.editionName())
                .publishers(openLibraryDto.publishers())
                .coverUrl(openLibraryDto.coverUrl())
                .work(work)
                .build();

        editionRepository.save(newEdition);

        LibraryItem newLibraryItem = LibraryItem.builder()
            .edition(newEdition)
            .appUser(appUserRepository.getReferenceById(userId))
            .build();

        libraryItemRepository.save(newLibraryItem);
    }
}
