package dev.jp.wordivore.dto;

import dev.jp.wordivore.model.ShelfStatus;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;
import java.util.List;


public record LibraryItemDto(
    Long itemId,
    String title,
    List<String> authors,
    List<String> subjectsTop4,
    String coverKey,
    ShelfStatus status,
    int pages,
    Integer publicationDate,
    LocalDate readStart,
    LocalDate readEnd
    ) { }