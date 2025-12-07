package com.sistema.domain;

import com.sistema.exception.InvalidEmailException;

public record Email(String value) {
    private static final String EMAIL_PATTERN = 
        "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
    
    public Email {
        if (value == null || value.trim().isEmpty()) {
            throw new InvalidEmailException("Email cannot be null or empty");
        }
        if (!value.matches(EMAIL_PATTERN)) {
            throw new InvalidEmailException("Invalid email format: " + value);
        }
    }
}