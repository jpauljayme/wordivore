package dev.jp.wordivore.service;

import dev.jp.wordivore.dto.WordOfTheDayResponse;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Slf4j
public class WordnikService {

    private final RestClient wordnikRestClient;

    @Value("${WORDNIK_API_KEY}")
    private String apiKey;

    @Cacheable(cacheNames = "wordnik:wotd", key = "#date", sync = true)
    @RateLimiter(name = "wordnik-rate-limiter")
    @Retry(name = "wordnik-retry", fallbackMethod = "getWordOfTheDayFallback")
    public WordOfTheDayResponse getWordOfTheDayFallback(LocalDate date){
        return wordnikRestClient.get()
                .uri(uri -> uri.path("/words.json/wordOfTheDay")
                        .queryParam("date", date)
                        .queryParam("api_key", apiKey)
                        .build()
                )
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(WordOfTheDayResponse.class);


    }

    public WordOfTheDayResponse getWordOfTheDayFallback(LocalDate date, Exception e){
        log.warn("Fallback triggered for wordnik word-of-the-day of date: {}, error {}", date, e.getMessage());
        return null;
    }
}
