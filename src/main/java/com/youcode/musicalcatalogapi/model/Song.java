package com.youcode.musicalcatalogapi.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "songs")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Song {
    @Id
    private String id;
    private String titre;
    private Integer duree;
    private Integer trackNumber;
    
    @DBRef
    private Album album;
} 