package com.sistema.domain;

import com.sistema.exception.InvalidAgeException;

public record Age(int value) {
    private static final int MIN_AGE = 0;
    private static final int MAX_AGE = 150;
    
    public Age {
        if (value < MIN_AGE || value > MAX_AGE) {
            throw new InvalidAgeException(
                "Age must be between " + MIN_AGE + " and " + MAX_AGE
            );
        }
    }
}