package com.sistema.exception;

public class DuplicatePersonException extends RuntimeException {
    public DuplicatePersonException(String message) {
        super(message);
    }
}