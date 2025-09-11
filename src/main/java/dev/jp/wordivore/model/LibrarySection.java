package dev.jp.wordivore.model;

import dev.jp.wordivore.dto.LibraryItemDto;

import java.util.List;

public record LibrarySection(
    ShelfStatus status,
    List<LibraryItemDto> books
) {
}
