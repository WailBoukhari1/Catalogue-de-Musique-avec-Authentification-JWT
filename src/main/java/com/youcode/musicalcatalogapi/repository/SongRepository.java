package com.youcode.musicalcatalogapi.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.youcode.musicalcatalogapi.model.Album;
import com.youcode.musicalcatalogapi.model.Song;

@Repository
public interface SongRepository extends MongoRepository<Song, String> {
    Page<Song> findByTitreContainingIgnoreCase(String titre, Pageable pageable);
    Page<Song> findByAlbum(Album album, Pageable pageable);
} 