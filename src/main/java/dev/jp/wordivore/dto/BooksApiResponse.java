package dev.jp.wordivore.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record BooksApiResponse(
        List<String> subjects,
        List<String> publishers,
        @JsonProperty("publish_places") List<String> publishedPlaces,
        @JsonProperty("number_of_pages") int pages,
        @JsonProperty("isbn_10") List<String> isbn10,
        @JsonProperty("isbn_13") List<String> isbn13
){}
