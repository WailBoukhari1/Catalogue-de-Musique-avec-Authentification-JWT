package com.youcode.musicalcatalogapi.model;

public enum Role {
    ROLE_USER,
    ROLE_ADMIN;

    public String getValue() {
        return this.name();
    }
} 