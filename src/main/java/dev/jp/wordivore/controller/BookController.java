package dev.jp.wordivore.controller;

import dev.jp.wordivore.dto.BookDto;
import dev.jp.wordivore.exception.BookDuplicateIsbnException;
import dev.jp.wordivore.exception.BookNotFoundException;
import dev.jp.wordivore.model.SecurityUser;
import dev.jp.wordivore.service.BookService;
import dev.jp.wordivore.service.OpenLibraryService;
import dev.jp.wordivore.service.S3Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.Objects;

@Controller
@RequiredArgsConstructor
@Slf4j
public class BookController {

    private final OpenLibraryService openLibraryService;
    private final BookService bookService;
    private final S3Service s3Service;


    @PostMapping("books/isbn")
    public String SearchBookByIsbn(Model model,
                                   @RequestParam String isbn,
                                   @AuthenticationPrincipal SecurityUser securityUser
    ) throws BookNotFoundException, InterruptedException, BookDuplicateIsbnException, IOException {
        BookDto bookDto = openLibraryService.searchByIsbn(isbn).orElse(null);

        if(Objects.nonNull(bookDto)){
//            bookService.insertBook(bookDto, isbn, securityUser.getUserId());
//            s3Service.listFolders();
            s3Service.uploadCover(isbn, bookDto.coverUrl());
        }

        model.addAttribute("books", bookService.getUserLibrary(securityUser.getUserId()));
        return "fragments/main :: userLibrary";
    }
}
