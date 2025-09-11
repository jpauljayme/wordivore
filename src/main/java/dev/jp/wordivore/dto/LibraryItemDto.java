package dev.jp.wordivore.dto;

import dev.jp.wordivore.model.ShelfStatus;

import java.util.List;


public record LibraryItemDto(
    Long itemId,
    String title,
    List<String> authors,
    List<String> subjectsTop4,
    String coverKey,
    ShelfStatus status,
    String editionName,
    int pages,
    Integer publicatioNDate
    ) { }