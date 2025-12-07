package com.sistema.web.dto;

public record ErrorDto(String message) {
    public ErrorDto {
        if (message == null || message.trim().isEmpty()) {
            throw new IllegalArgumentException("Error message cannot be null or empty");
        }
    }
}