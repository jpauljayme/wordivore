package dev.jp.wordivore.controller;

import dev.jp.wordivore.dto.LibraryItemDto;
import dev.jp.wordivore.dto.OpenLibraryDto;
import dev.jp.wordivore.exception.BookNotFoundException;
import dev.jp.wordivore.exception.LibraryItemNotFoundException;
import dev.jp.wordivore.exception.OpenLibraryWorkNotFoundException;
import dev.jp.wordivore.model.LibrarySection;
import dev.jp.wordivore.model.SecurityUser;
import dev.jp.wordivore.model.ShelfStatus;
import dev.jp.wordivore.repository.LibraryItemRepository;
import dev.jp.wordivore.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/books")
@RequiredArgsConstructor
@Slf4j
public class BookController {

    private final OpenLibraryService openLibraryService;
    private final ShelfReadService shelfReadService;
    private final ShelfWriteService shelfWriteService;
    private final LibraryItemRepository libraryItemRepository;

    @Value("${CLOUDFRONT_URL}")
    private String prefix;

    @PostMapping("/isbn")
    public String SearchBookByIsbn(Model model,
                                   @RequestParam String isbn,
                                   @AuthenticationPrincipal SecurityUser securityUser
    ) throws BookNotFoundException, OpenLibraryWorkNotFoundException, InterruptedException, IOException {
        OpenLibraryDto bookDto = openLibraryService.searchByIsbn(isbn).
                orElseGet(() -> null);

        if (Objects.nonNull(bookDto)) {
            shelfWriteService.insertBook(bookDto, isbn, securityUser.getUserId());
        }

        List<LibrarySection> library = shelfReadService.getUserLibraryAllSections(securityUser.getUserId());

        List<LibraryItemDto> libraryToRead = library.getFirst().books();
        model.addAttribute("libraryToRead", libraryToRead);
        model.addAttribute("libraryToReadCount", libraryToRead.size());

        List<LibraryItemDto> libraryCurrentReads = library.get(1).books();
        model.addAttribute("libraryCurrentReads", libraryCurrentReads);
        model.addAttribute("libraryCurrentReadsCount", libraryCurrentReads.size());

        model.addAttribute("prefix", prefix);

        return "fragments/main :: userLandingMain";
    }

    @GetMapping("/currently-reading")
    public String viewCurrentReads(Model model, @AuthenticationPrincipal SecurityUser securityUser) {


        model.addAttribute("appUser", securityUser.getAppUser());

        List<LibraryItemDto> libraryCurrentReads = shelfReadService.getUserLibraryByStatus(securityUser.getUserId(), ShelfStatus.CURRENTLY_READING);
        model.addAttribute("libraryCurrentReads", libraryCurrentReads);
        model.addAttribute("libraryCurrentReadsCount", libraryCurrentReads.size());
        model.addAttribute("prefix", prefix);
        model.addAttribute("shelfStatusValues", ShelfStatus.values());


        return "fragments/main :: currentReads";
    }

    @GetMapping("/to-read")
    public String viewToRead(Model model, @AuthenticationPrincipal SecurityUser securityUser) {


        model.addAttribute("appUser", securityUser.getAppUser());

        List<LibraryItemDto> libraryToRead = shelfReadService.getUserLibraryByStatus(securityUser.getUserId(), ShelfStatus.TO_READ);
        model.addAttribute("libraryToRead", libraryToRead);
        model.addAttribute("libraryToReadCount", libraryToRead.size());
        model.addAttribute("prefix", prefix);
        model.addAttribute("shelfStatusValues", ShelfStatus.values());


        return "fragments/main :: libraryToReadAll";
    }

    @PostMapping("/{id}/status")
    public String updateShelfStatus(Model model,
                                    @RequestParam ShelfStatus status,
                                    @PathVariable Long id,
                                    @AuthenticationPrincipal SecurityUser securityUser
    ) throws LibraryItemNotFoundException {

        LibraryItemDto libraryItemDto = shelfWriteService.updateShelfStatus(securityUser.getUserId(), id, status);

        model.addAttribute("b", libraryItemDto);
        model.addAttribute("prefix", prefix);
        model.addAttribute("shelfStatusValues", ShelfStatus.values());

        return "fragments/edition :: shelfItem";
    }

    @GetMapping("/{id}")
    public String viewBook(Model model,
                           @PathVariable Long id
    ) throws BookNotFoundException {

        LibraryItemDto libraryItemDto = shelfReadService.getBookById(id).orElseGet( () -> new LibraryItemDto(0L,
                "",
                Collections.emptyList(),
                Collections.emptyList(),
                "",
                ShelfStatus.DNF,
                0,
                0,
                LocalDate.now(),
                LocalDate.now()
        ));

        model.addAttribute("b", libraryItemDto);
        model.addAttribute("prefix", prefix);
        model.addAttribute("shelfStatusValues", ShelfStatus.values());

        return "fragments/edition :: shelfItem";
    }
}
