package dev.jp.wordivore.dto;

import java.util.List;

public record BookDto(
        List<String> authors,
        int publicationDate,
        String title,
        List<String> subjects,
        int pages,
        List<String> isbn10
) {
}
