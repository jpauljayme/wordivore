package dev.jp.emancipate_the_self.controller;

import dev.jp.emancipate_the_self.dto.BookDto;
import dev.jp.emancipate_the_self.exception.BookNotFoundException;
import dev.jp.emancipate_the_self.service.OpenLibraryService;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BookController {

    private final OpenLibraryService openLibraryService;

    public BookController(OpenLibraryService searchService) {
        this.openLibraryService = searchService;
    }

    @GetMapping("/books/title")
    public String SearchBookByTitle(@RequestParam String title){
        String result = openLibraryService.searchByTitle(title);
        System.out.println(result);
        return result;
    }

    @GetMapping("books/isbn")
    public ResponseEntity<BookDto> SearchBookByIsbn(@RequestParam String isbn) throws BookNotFoundException {
        BookDto bookDto = openLibraryService.searchByIsbn(isbn).orElse(null);
        return ResponseEntity.ok().body(bookDto);
    }

    @GetMapping("/hello")
    public String index(Model model){
        return "hello";
    }
}
