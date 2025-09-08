package dev.jp.wordivore.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.jp.wordivore.dto.OpenLibraryDto;
import dev.jp.wordivore.dto.BooksApiResponse;
import dev.jp.wordivore.dto.SearchApiResponse;
import dev.jp.wordivore.dto.WorkResponseDto;
import dev.jp.wordivore.exception.BookNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.RestClient;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class OpenLibraryService {
    private final S3Service s3Service;
    private final RestClient openLibraryRestClient;
    private final ObjectMapper objectMapper;
//              covers.api.baseurl
    @Value("${covers.api.baseurl}")
    private String coverBaseUrl;

    @Value("${WORDIVORE_MISSING_COVER_URL}")
    private String missingCoverUrl;


    public String searchByTitle(String title) {
        return openLibraryRestClient.get()
                .uri("/search.json?q=title:{title}", title)
                .retrieve()
                .body(String.class);
    }

    public Optional<OpenLibraryDto> searchByIsbn(String isbn) throws IOException, InterruptedException {
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

        String coverKey = s3Service.uploadCover(isbn, coverUrl)
                .orElse(missingCoverUrl);

        List<String> subjects = booksApiResponse != null && booksApiResponse.subjects() != null? booksApiResponse.subjects() : Collections.<String>emptyList();
        int pages = booksApiResponse != null ? booksApiResponse.pages() : 0;
        List<String> isbn10 = booksApiResponse != null && booksApiResponse.isbn10()  != null ? booksApiResponse.isbn10() : Collections.<String>emptyList();
        List<String> isbn13 = booksApiResponse != null && booksApiResponse.isbn13()  != null ? booksApiResponse.isbn13() : Collections.<String>emptyList();
        List<String> publishers = booksApiResponse != null && booksApiResponse.publishers()  != null ? booksApiResponse.publishers() : Collections.emptyList();
        String editionName = booksApiResponse != null && booksApiResponse.editionName()  != null ? booksApiResponse.editionName() : "";
        List<String> publishedPlaces = booksApiResponse != null && booksApiResponse.publishedPlaces() != null ? booksApiResponse.publishedPlaces() : Collections.<String>emptyList();

        //TODO: booksapiresponse fields can be null like edition name page isbn ... so i need another check condition.
        return Optional.of(new OpenLibraryDto(
                first.authors(),
                first.publicationDate(),
                publishers,
                first.title(),
                editionName,
                subjects,
                pages,
                isbn10,
                isbn13,
                first.key(),
                publishedPlaces,
                coverKey,
                coverUrl
        ));
    }

    public WorkResponseDto findWorkByKey(String key){


        return openLibraryRestClient.get()
                .uri("/{key}.json", key)
                .retrieve()
                .body(WorkResponseDto.class);


    }
}
