package dev.jp.wordivore.service;

import dev.jp.wordivore.dto.OpenLibraryDto;
import dev.jp.wordivore.dto.WorkResponseDto;
import dev.jp.wordivore.exception.BookDuplicateIsbnException;
import dev.jp.wordivore.exception.BookNotFoundException;
import dev.jp.wordivore.exception.OpenLibraryWorkNotFoundException;
import dev.jp.wordivore.mapstruct.OpenLibraryEditionMapper;
import dev.jp.wordivore.model.*;
import dev.jp.wordivore.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

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
//        return bookRepository.findAllByAppUser_Id(id);
        return libraryItemRepository.findAllByAppUser_Id(id);
    }

    public List<LibraryItem> getUserLibraryMostRecent(Long id) {
//        return bookRepository.findTop3ByAppUser_IdOrderByCreatedAtDesc(id);
        return libraryItemRepository.findTop3ByAppUser_IdOrderByCreatedAtDesc(id);
    }


    public void insertBook(OpenLibraryDto openLibraryDto, String isbn, Long userId) throws OpenLibraryWorkNotFoundException {

        Edition edition = (isbn != null && isbn.length() == 10) ?
            editionRepository.findByIsbn10(isbn).orElse(null)
            : editionRepository.findByIsbn13(isbn).orElse(null);

        //If edition exists, just attach to library item for user and then exit if not yet added.
        if(edition != null){

             if(!libraryItemRepository.existsByAppUser_IdAndEdition_Id(userId, edition.id)){
                 LibraryItem newLibraryItem = LibraryItem.builder()
                         .appUser(appUserRepository.getReferenceById(userId))
                         .status(ShelfStatus.TO_READ.getStatus())
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

                    if(workResponseDto != null && workResponseDto.key() != null && !workResponseDto.key().isEmpty()){
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

        if(work == null) throw new OpenLibraryWorkNotFoundException();

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
            .status(ShelfStatus.TO_READ.getStatus())
            .build();

        libraryItemRepository.save(newLibraryItem);
    }
}
