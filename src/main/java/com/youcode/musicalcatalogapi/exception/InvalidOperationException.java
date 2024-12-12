package com.youcode.musicalcatalogapi.exception;

public class InvalidOperationException extends BadRequestException {
    public InvalidOperationException(String message) {
        super(message);
    }
} 