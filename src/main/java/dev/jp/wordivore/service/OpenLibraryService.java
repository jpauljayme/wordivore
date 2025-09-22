package dev.jp.wordivore.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.jp.wordivore.dto.BooksApiResponse;
import dev.jp.wordivore.dto.OpenLibraryDto;
import dev.jp.wordivore.dto.SearchApiResponse;
import dev.jp.wordivore.dto.WorkResponseDto;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClient;

import java.util.Collections;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
@Slf4j
public class OpenLibraryService {
    private final S3Service s3Service;
    private final RestClient openLibraryRestClient;

    @Value("${covers.api.baseurl}")
    private String coverBaseUrl;

    @Value("${WORDIVORE_MISSING_COVER_URL}")
    private String missingCoverUrl;

    private final ObjectMapper objectMapper;

    private static final String FIELDS = "key,title,author_name,editions,number_of_pages_median,editions.publish_date,editions.publisher,first_sentence,subject,editions.isbn";

    @Cacheable(cacheNames = "ol:isbn", key = "#isbn", sync = true)
    @RateLimiter(name = "openlibrary-rate-limiter")
    @Retry(name = "openlibrary-retry", fallbackMethod = "searchByIsbnFallback")
    public Optional<OpenLibraryDto> searchByIsbn(String isbn) {

        try {
            String raw = openLibraryRestClient.get()
                    .uri(uri -> uri.path("/search.json")
                            .queryParam("isbn", isbn)
                            .queryParam("fields", FIELDS)
                            .build()
                    )
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .body(String.class);

            log.info(raw);

            SearchApiResponse search = objectMapper.readValue(raw, SearchApiResponse.class);

            if (ObjectUtils.isEmpty(search) || ObjectUtils.isEmpty(search.docs())) {
                log.info("No results found for isbn {}", isbn);
                return Optional.empty();
            }

            var firstSearchDto = search.docs().getFirst();

            String coverUrl = coverBaseUrl + isbn;

            String coverKey = s3Service.uploadCover(isbn, coverUrl)
                    .orElse(missingCoverUrl);

            int pages = firstSearchDto.pages() != null ? firstSearchDto.pages() : 0;

            var firstEditionDocs = firstSearchDto.editionInfo().docs().getFirst();

            String isbn_first = firstEditionDocs.isbns().getFirst();
            String isbn_sec = firstEditionDocs.isbns().get(1);

            String isbn13 = isbn_first.length() == 13 ? isbn_first : "";
            String isbn10 = isbn_sec.length() == 10 ? isbn_sec : "";

            List<String> publishers = firstEditionDocs.publishers() != null ? firstEditionDocs.publishers(): Collections.emptyList();

            Integer publicationDate = firstSearchDto.firstPublishedYear()!= null ? firstSearchDto.firstPublishedYear() : 0;

            return Optional.of(new OpenLibraryDto(
                    firstSearchDto.authors(),
                    publicationDate,
                    publishers,
                    firstSearchDto.title(),
                    pages,
                    isbn10,
                    isbn13,
                    firstSearchDto.key(),
                    coverKey,
                    coverUrl
            ));
        }catch (JsonProcessingException e) {
            log.error("JSON parsing failed for ISBN: {}. Error: {}", isbn, e.getMessage());
            throw new RuntimeException("JSON parsing failed", e);
        }
    }

    @RateLimiter(name = "openlibrary-rate-limiter")
    @Retry(name = "openlibrary-retry", fallbackMethod = "findByWorkKeyFallback")
    public WorkResponseDto findWorkByKey(String key){
        return openLibraryRestClient.get()
                .uri("/{key}.json", key)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, (request, response) -> {
                    if(response.getStatusCode() == HttpStatus.TOO_MANY_REQUESTS){
                        log.warn("Rate limit exceeded for isbn: {}", key);
                        throw new HttpClientErrorException(HttpStatus.TOO_MANY_REQUESTS, "OpenLibrary rate limit exceeded.");
                    }
                    throw new RuntimeException("API request failed for " + key +  " with status " + response.getStatusCode() );
                }

                )
                .body(WorkResponseDto.class);
    }


    public String searchByTitle(String title) {
        return openLibraryRestClient.get()
                .uri("/search.json?q=title:{title}", title)
                .retrieve()
                .body(String.class);
    }

    @Retry(name = "openlibrary-retry", fallbackMethod = "getBookDetailsFallback")
    private BooksApiResponse getBookDetailsWithRetry(String isbn){
        log.debug("Fetching book details thru isbn api with id: {}", isbn);

        return openLibraryRestClient.get()
                .uri("/books/{isbn}.json", isbn)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, (request, response) -> {
                    if(response.getStatusCode() == HttpStatus.TOO_MANY_REQUESTS) {
                        log.warn("Rate limit for isbn {} will retry ", isbn);
                        throw new HttpClientErrorException(HttpStatus.TOO_MANY_REQUESTS);
                    }

                    log.warn("Failed to fetch book details thru the ISBN API for isbn: {}, status: {}", isbn, response.getStatusCode());

                    throw new HttpClientErrorException(response.getStatusCode());
                })
                .onStatus(HttpStatusCode::is5xxServerError, (request, response) -> {
                    log.error("Server error for ISBN: {} and status: {}", isbn, response.getStatusCode());
                    throw new HttpServerErrorException(response.getStatusCode());
                })
                .body(BooksApiResponse.class);
    }

    public Optional<OpenLibraryDto> searchByIsbnFallback(String isbn, Exception e){
        log.warn("Fallback triggered for ISBN: {}, error: {}", isbn, e.getMessage());
        return Optional.empty();
    }

    public WorkResponseDto findByWorkKeyFallback(String key, Exception e){
        log.warn("Find work by key fallback for key {} and error: {}", key, e.getMessage());
        return null;
    }
}
