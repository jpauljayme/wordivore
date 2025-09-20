package dev.jp.wordivore.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record Docs(
        @JsonProperty("author_name") List<String> authors,
        @JsonProperty("contributor") List<String> contributors,
        @JsonProperty("first_sentence") List<String> firstSentence,
        @JsonProperty("first_publish_year") Integer firstPublishedYear,
        @JsonProperty("number_of_pages_median") Integer pages,

        String title,
        String key,
        @JsonProperty("editions") EditionsResponse editionInfo
        ){}
