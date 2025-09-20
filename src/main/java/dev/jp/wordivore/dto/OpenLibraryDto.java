package dev.jp.wordivore.dto;

import java.time.LocalDate;
import java.util.List;

public record OpenLibraryDto(
        List<String> authors,
        Integer publicationDate,
        List<String> publishers,
        String title,
        int pages,
        String isbn10,
        String isbn13,
        String key,
        String coverKey,
        String coverUrl
) {
}
