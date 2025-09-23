package dev.jp.wordivore.exception;

public class LibraryItemNotFoundException extends Exception {
    public LibraryItemNotFoundException(){
        super("Library Item not found");
    }
}
