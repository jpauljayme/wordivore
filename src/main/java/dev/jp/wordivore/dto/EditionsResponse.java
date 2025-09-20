package dev.jp.wordivore.dto;

import java.util.List;

public record EditionsResponse(
    List<EditionDoc> docs
) {
}
