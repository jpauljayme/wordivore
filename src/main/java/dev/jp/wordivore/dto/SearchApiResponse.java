package dev.jp.wordivore.dto;

import java.util.List;

public record SearchApiResponse(
    List<Docs> docs
) {
}

