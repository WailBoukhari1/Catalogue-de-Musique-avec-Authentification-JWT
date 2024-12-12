package com.youcode.musicalcatalogapi.exception;

public class DuplicateResourceException extends BadRequestException {
    public DuplicateResourceException(String message) {
        super(message);
    }
} 