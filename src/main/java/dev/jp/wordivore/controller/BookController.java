package dev.jp.wordivore.controller;

import dev.jp.wordivore.dto.OpenLibraryDto;
import dev.jp.wordivore.exception.BookDuplicateIsbnException;
import dev.jp.wordivore.exception.BookNotFoundException;
import dev.jp.wordivore.exception.OpenLibraryWorkNotFoundException;
import dev.jp.wordivore.model.SecurityUser;
import dev.jp.wordivore.service.LibraryItemService;
import dev.jp.wordivore.service.OpenLibraryService;
import dev.jp.wordivore.service.S3Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.Objects;

@Controller
@RequiredArgsConstructor
@Slf4j
public class BookController {

    private final OpenLibraryService openLibraryService;
    private final LibraryItemService libraryItemService;
    private final S3Service s3Service;

    @Value("${CLOUDFRONT_URL}")
    private String prefix;

    @PostMapping("books/isbn")
    public String SearchBookByIsbn(Model model,
                                   @RequestParam String isbn,
                                   @AuthenticationPrincipal SecurityUser securityUser
    ) throws BookNotFoundException, OpenLibraryWorkNotFoundException, InterruptedException , IOException {
        OpenLibraryDto bookDto = openLibraryService.searchByIsbn(isbn).
            orElseGet(() -> null);

        if(Objects.nonNull(bookDto)){
            libraryItemService.insertBook(bookDto, isbn, securityUser.getUserId());
//            if(!bookDto.coverUrl().isEmpty()){
//                s3Service.uploadCover(isbn, bookDto.coverUrl());
//            }
        }

        model.addAttribute("books", libraryItemService.getUserLibraryMostRecent(securityUser.getUserId()));
        model.addAttribute("prefix", prefix);
        return "fragments/main :: list";
    }

    @GetMapping("/books/all")
    public String viewAllBooks(Model model, @AuthenticationPrincipal SecurityUser securityUser){
        model.addAttribute("username", securityUser.getUsername());
        model.addAttribute("books", libraryItemService.getUserLibrary(securityUser.getUserId()));
        model.addAttribute("prefix", prefix);

        return "fragments/main :: viewAll";
    }
}
