package dev.jp.wordivore.controller;

import dev.jp.wordivore.dto.BookDto;
import dev.jp.wordivore.exception.BookDuplicateIsbnException;
import dev.jp.wordivore.exception.BookNotFoundException;
import dev.jp.wordivore.model.SecurityUser;
import dev.jp.wordivore.service.BookService;
import dev.jp.wordivore.service.OpenLibraryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Objects;

@Controller
@RequiredArgsConstructor
@Slf4j
public class BookController {

    private final OpenLibraryService openLibraryService;
    private final BookService bookService;

//    @GetMapping("/books/title")
//    public String SearchBookByTitle(@RequestParam String title){
//        String result = openLibraryService.searchByTitle(title);
//        return result;
//    }

    @PostMapping("books/isbn")
//    @HxRetarget(value = "#userLibrary")
//    @HxReswap(value = HxSwapType.OUTER_HTML)
    public String SearchBookByIsbn(Model model,
                                   @RequestParam String isbn,
                                   @AuthenticationPrincipal SecurityUser securityUser
    ) throws BookNotFoundException {
        BookDto bookDto = openLibraryService.searchByIsbn(isbn).orElse(null);

        if(Objects.nonNull(bookDto)){
            try {
                bookService.insertBook(bookDto, isbn, securityUser.getUserId());
            } catch (BookDuplicateIsbnException e) {
               log.error(e.getMessage());
            }
        }

        model.addAttribute("books", bookService.getUserLibrary(securityUser.getUserId()));
        return "fragments/main :: userLibrary";
    }
}
