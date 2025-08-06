package dev.jp.emancipate_the_self.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record SearchApiResponse(
    List<Docs> docs
) {
}

