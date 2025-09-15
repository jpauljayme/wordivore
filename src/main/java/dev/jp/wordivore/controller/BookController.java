package dev.jp.wordivore.controller;

import dev.jp.wordivore.dto.LibraryItemDto;
import dev.jp.wordivore.dto.OpenLibraryDto;
import dev.jp.wordivore.exception.BookNotFoundException;
import dev.jp.wordivore.exception.OpenLibraryWorkNotFoundException;
import dev.jp.wordivore.model.LibrarySection;
import dev.jp.wordivore.model.SecurityUser;
import dev.jp.wordivore.model.ShelfStatus;
import dev.jp.wordivore.service.*;
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
import java.util.List;
import java.util.Objects;

@Controller
@RequiredArgsConstructor
@Slf4j
public class BookController {

    private final OpenLibraryService openLibraryService;
    private final ShelfReadService shelfReadService;
    private final ShelfWriteService shelfWriteService;

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
            shelfWriteService.insertBook(bookDto, isbn, securityUser.getUserId());
        }

        List<LibrarySection> library = shelfReadService.getUserLibraryAllSections(securityUser.getUserId());

        List<LibraryItemDto> libraryToRead = library.getFirst().books();
        model.addAttribute("libraryToRead", libraryToRead);
        model.addAttribute("libraryToReadCount", libraryToRead.size());

        List<LibraryItemDto> libraryCurrentReads = library.get(1).books();
        model.addAttribute("libraryCurrentReads", libraryCurrentReads );
        model.addAttribute("libraryCurrentReadsCount", libraryCurrentReads.size());

        model.addAttribute("prefix", prefix);

        return "fragments/main :: userLandingMain";
    }

    @GetMapping("/books/currently-reading")
    public String viewCurrentReads(Model model, @AuthenticationPrincipal SecurityUser securityUser){


        model.addAttribute("appUser", securityUser.getAppUser());

        List<LibraryItemDto> libraryCurrentReads = shelfReadService.getUserLibraryCurrentReads(securityUser.getUserId());
        model.addAttribute("libraryCurrentReads", libraryCurrentReads);
        model.addAttribute("libraryCurrentReadsCount", libraryCurrentReads.size());
        model.addAttribute("prefix", prefix);
        model.addAttribute("shelfStatusValues", ShelfStatus.values());


        return "fragments/main :: currentReads";
    }
}
