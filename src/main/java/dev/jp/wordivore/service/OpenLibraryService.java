package dev.jp.wordivore.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.jp.wordivore.dto.BookDto;
import dev.jp.wordivore.dto.BooksApiResponse;
import dev.jp.wordivore.dto.Docs;
import dev.jp.wordivore.dto.SearchApiResponse;
import dev.jp.wordivore.exception.BookNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.RestClient;

import java.net.URI;
import java.util.Collections;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class OpenLibraryService {
    private final RestClient openLibraryRestClient;
    private final ObjectMapper objectMapper;
//              covers.api.baseurl
    @Value("${covers.api.baseurl}")
    private String coverBaseUrl;


    public String searchByTitle(String title) {
        return openLibraryRestClient.get()
                .uri("/search.json?q=title:{title}", title)
                .retrieve()
                .body(String.class);
    }

    public Optional<BookDto> searchByIsbn(String isbn) throws BookNotFoundException {
        SearchApiResponse search = openLibraryRestClient.get()
                .uri(uri -> uri.path("/search.json")
                        .queryParam("isbn", isbn)
                        .build()
                )
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(httpStatusCode -> httpStatusCode != HttpStatus.OK, ((request, response) -> {
                    throw new RuntimeException("API Execution request failed with status : " + response.getStatusCode());
                }))
                .body(SearchApiResponse.class);

        if (ObjectUtils.isEmpty(search) || ObjectUtils.isEmpty(search.docs())) {
            return Optional.empty();
        }

        var first = search.docs().getFirst();

        BooksApiResponse booksApiResponse = openLibraryRestClient.get()
                .uri("/isbn/{isbn}.json", isbn)
                .retrieve()
                .body(BooksApiResponse.class);

        String coverUrl = coverBaseUrl + isbn;
        return Optional.of(new BookDto(
                first.authors(),
                first.publicationDate(),
                first.title(),
                booksApiResponse != null ? booksApiResponse.subjects() : Collections.emptyList(),
                booksApiResponse != null ? booksApiResponse.pages() : 0,
                booksApiResponse != null ? booksApiResponse.isbn10() : Collections.emptyList(),
                coverUrl
        ));
    }
}
