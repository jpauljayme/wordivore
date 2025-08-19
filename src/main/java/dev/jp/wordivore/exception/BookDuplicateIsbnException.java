package dev.jp.wordivore.exception;

public class BookDuplicateIsbnException extends Exception{
    public BookDuplicateIsbnException() {
        super("Book already exists by ISBN.");
    }
}
