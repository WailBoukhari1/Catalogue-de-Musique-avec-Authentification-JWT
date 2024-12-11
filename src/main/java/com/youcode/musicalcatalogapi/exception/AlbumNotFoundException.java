package com.youcode.musicalcatalogapi.exception;

public class AlbumNotFoundException extends ResourceNotFoundException {
    public AlbumNotFoundException(String message) {
        super(message);
    }
} 