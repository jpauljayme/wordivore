package dev.jp.wordivore.controller;

import dev.jp.wordivore.dto.LibraryItemDto;
import dev.jp.wordivore.dto.OpenLibraryDto;
import dev.jp.wordivore.exception.BookNotFoundException;
import dev.jp.wordivore.exception.OpenLibraryWorkNotFoundException;
import dev.jp.wordivore.model.LibrarySection;
import dev.jp.wordivore.model.SecurityUser;
import dev.jp.wordivore.model.ShelfStatus;
import dev.jp.wordivore.service.AppUserService;
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
import java.time.LocalDate;
import java.util.EnumMap;
import java.util.List;
import java.util.Objects;

@Controller
@RequiredArgsConstructor
@Slf4j
public class BookController {

    private final OpenLibraryService openLibraryService;
    private final LibraryItemService libraryItemService;
    private final AppUserService appUserService;
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
        }

        return "redirect:/user";
    }

    @GetMapping("/books/currently-reading")
    public String viewCurrentReads(Model model, @AuthenticationPrincipal SecurityUser securityUser){


        model.addAttribute("appUser", securityUser.getAppUser());

        List<LibraryItemDto> libraryCurrentReads = libraryItemService.getUserLibraryCurrentReads(securityUser.getUserId());
        model.addAttribute("libraryCurrentReads", libraryCurrentReads);
        model.addAttribute("libraryCurrentReadsCount", libraryCurrentReads.size());
        model.addAttribute("prefix", prefix);
        model.addAttribute("shelfStatusValues", ShelfStatus.values());


        return "fragments/main :: currentReads";
    }

    @PostMapping("/saveDate")
    public String saveDate(LocalDate date, Long libraryItemId, Model model) throws BookNotFoundException {
        libraryItemService.saveLibraryItemDate(libraryItemId, date);

        return "fragments/main :: currentReads";
    }
}
