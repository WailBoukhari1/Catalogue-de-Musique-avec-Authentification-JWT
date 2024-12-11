package com.youcode.musicalcatalogapi.exception;

public class SongNotFoundException extends ResourceNotFoundException {
    public SongNotFoundException(String message) {
        super(message);
    }
} 