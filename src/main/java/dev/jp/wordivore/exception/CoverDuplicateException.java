package dev.jp.wordivore.exception;

public class CoverDuplicateException extends Exception{
    public CoverDuplicateException() {
        super("Cover already exists in the bucket.");
    }
}
