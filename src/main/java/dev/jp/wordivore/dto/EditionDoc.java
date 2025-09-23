package dev.jp.wordivore.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.util.List;

public record EditionDoc(
        @JsonProperty("publisher") List<String> publishers,
        @JsonProperty("isbn") List<String> isbns
){
}
