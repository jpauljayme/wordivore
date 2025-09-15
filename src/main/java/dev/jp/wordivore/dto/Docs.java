package dev.jp.wordivore.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record Docs(
        @JsonProperty("author_name") List<String> authors,
        @JsonProperty("first_publish_year")int publicationDate,
        @JsonProperty("title")String title,
        String key
        ){}
