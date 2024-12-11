package com.youcode.musicalcatalogapi.model;

import org.springframework.data.annotation.Id;
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
} 