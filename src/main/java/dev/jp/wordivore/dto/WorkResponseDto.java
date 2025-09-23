package dev.jp.wordivore.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record WorkResponseDto(
    String description,
    List<WorkLinks> links,
    String title,
    String key,
    List<String> subjects,
    @JsonProperty("first_sentence")
    WorkFirstSentence firstSentence
) {}

