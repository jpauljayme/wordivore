package dev.jp.wordivore.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.jp.wordivore.dto.BookDto;
import dev.jp.wordivore.dto.BooksApiResponse;
import dev.jp.wordivore.dto.Docs;
import dev.jp.wordivore.dto.SearchApiResponse;
import dev.jp.wordivore.exception.BookNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.net.URI;
import java.util.Collections;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class OpenLibraryService {
    private final RestClient openLibraryRestClient;
    private final ObjectMapper objectMapper;


    public String searchByTitle(String title) {
        return openLibraryRestClient.get()
                .uri("/search.json?q=title:{title}", title)
                .retrieve()
                .body(String.class);
    }

    public Optional<BookDto> searchByIsbn(String isbn) throws BookNotFoundException {
        SearchApiResponse searchApiResponse = openLibraryRestClient.get()
                .uri("/search.json?q=isbn:{isbn}", isbn)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(httpStatusCode -> httpStatusCode != HttpStatus.OK, ((request, response) -> {
                    throw new RuntimeException("API Execution request failed with status : " + response.getStatusCode());
                }))
                .body(SearchApiResponse.class);

        ResponseEntity<Void> redirectResponse = openLibraryRestClient.get()
                .uri("/isbn/{isbn}.json", isbn)
                .retrieve()
                .toBodilessEntity();

        URI redirectedUri = redirectResponse.getHeaders().getLocation();
        if(redirectedUri == null){
           throw new BookNotFoundException();
        }

        BooksApiResponse booksApiResponse = openLibraryRestClient.get()
                .uri(redirectedUri)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(BooksApiResponse.class);

        Docs first = null;
        if (searchApiResponse != null) {
            first = searchApiResponse.docs().getFirst();
            return Optional.of(new BookDto(
                    first.authors(),
                    first.publicationDate(),
                    first.title(),
                    booksApiResponse != null ? booksApiResponse.subjects() : Collections.emptyList(),
                    booksApiResponse != null ? booksApiResponse.pages() : 0,
                    booksApiResponse != null ? booksApiResponse.isbn10() : Collections.emptyList()
            ));
        }
        return Optional.empty();
    }
}
