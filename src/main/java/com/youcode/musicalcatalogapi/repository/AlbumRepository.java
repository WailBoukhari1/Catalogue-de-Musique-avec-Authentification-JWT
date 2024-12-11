package com.youcode.musicalcatalogapi.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.youcode.musicalcatalogapi.model.Album;

@Repository
public interface AlbumRepository extends MongoRepository<Album, String> {
    Page<Album> findByTitreContainingIgnoreCase(String titre, Pageable pageable);
    Page<Album> findByArtisteContainingIgnoreCase(String artiste, Pageable pageable);
    Page<Album> findByAnnee(Integer annee, Pageable pageable);
} 