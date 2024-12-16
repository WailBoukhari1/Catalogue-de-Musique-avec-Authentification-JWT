package com.youcode.musicalcatalogapi.model;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "albums")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Album {
    @Id
    private String id;
    private String titre;
    private String artiste;
    private Integer annee;
    @DBRef
    private List<Song> songs;
} 