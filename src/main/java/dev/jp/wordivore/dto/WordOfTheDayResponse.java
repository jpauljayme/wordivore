package dev.jp.wordivore.dto;

import java.time.LocalDate;
import java.util.List;

public record WordOfTheDayResponse(
        String word,
        String note,
        LocalDate pdd,
        List<WordOfTheDayDefinitions> definitions,
        List<WordOfTheDayExample> examples
        ) {
}
