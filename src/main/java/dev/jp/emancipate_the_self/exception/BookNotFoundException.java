package dev.jp.emancipate_the_self.exception;

public class BookNotFoundException extends Exception{
    public BookNotFoundException() {
        super("Book not found");
    }
}
