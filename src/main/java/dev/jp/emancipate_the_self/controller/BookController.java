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
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
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

    @GetMapping("books/isbn")
    public ResponseEntity<BookDto> SearchBookByIsbn(@RequestParam String isbn,
                                                    @AuthenticationPrincipal SecurityUser securityUser
                                                    ) throws BookNotFoundException {
        BookDto bookDto = openLibraryService.searchByIsbn(isbn).orElse(null);
        bookService.insertBook(bookDto, securityUser.getUserId());
        return ResponseEntity.ok().body(bookDto);
    }
}
