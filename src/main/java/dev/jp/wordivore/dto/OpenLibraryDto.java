package dev.jp.wordivore.dto;

import java.time.LocalDate;
import java.util.List;

public record OpenLibraryDto(
        List<String> authors,
        Integer publicationDate,
        List<String> publishers,
        String title,
        String editionName,
        List<String> subjects,
        int pages,
        List<String> isbn10,
        List<String> isbn13,
        String key,
        List<String> publishedPlaces,
        String coverKey,
        String coverUrl
) {
}
