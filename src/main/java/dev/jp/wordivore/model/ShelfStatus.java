package dev.jp.wordivore.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ShelfStatus { TO_READ("to read"), CURRENTLY_READING("currently reading"), READ("read"), DNF("did not finish");

    private final String status;
}
