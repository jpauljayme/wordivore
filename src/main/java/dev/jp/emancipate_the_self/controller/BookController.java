package dev.jp.emancipate_the_self.controller;

import dev.jp.emancipate_the_self.dto.BookDto;
import dev.jp.emancipate_the_self.exception.BookNotFoundException;
import dev.jp.emancipate_the_self.model.SecurityUser;
import dev.jp.emancipate_the_self.service.BookService;
import dev.jp.emancipate_the_self.service.OpenLibraryService;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RequiredArgsConstructor
public class BookController {

    private final OpenLibraryService openLibraryService;
    private final BookService bookService;
    private final EntityManager em;

    @GetMapping("/books/title")
    public String SearchBookByTitle(@RequestParam String title){
        String result = openLibraryService.searchByTitle(title);
        System.out.println(result);
        return result;
    }

    @PostMapping("books/isbn")
    public String SearchBookByIsbn(Model model,
                                   @RequestParam String isbn,
                                   @AuthenticationPrincipal SecurityUser securityUser
    ) throws BookNotFoundException {
        BookDto bookDto = openLibraryService.searchByIsbn(isbn).orElse(null);
        bookService.insertBook(bookDto, securityUser.getUserId());
        return "index";
    }
}
