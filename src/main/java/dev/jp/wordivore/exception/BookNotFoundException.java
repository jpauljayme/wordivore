package dev.jp.wordivore.exception;

public class BookNotFoundException extends Exception{
    public BookNotFoundException() {
        super("Book not found");
    }
}
