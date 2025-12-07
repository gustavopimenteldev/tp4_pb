package com.sistema.domain;

public record PersonId(long value) {
    public PersonId {
        if (value <= 0) {
            throw new IllegalArgumentException("PersonId must be positive");
        }
    }
}