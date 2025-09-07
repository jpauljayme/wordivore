package dev.jp.wordivore.exception;

public class OpenLibraryWorkNotFoundException extends Exception{
    public OpenLibraryWorkNotFoundException() {
        super("Work not found through Open Library Api");
    }
}
