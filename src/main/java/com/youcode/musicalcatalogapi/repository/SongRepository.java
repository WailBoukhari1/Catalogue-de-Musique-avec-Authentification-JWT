package com.youcode.musicalcatalogapi.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.youcode.musicalcatalogapi.model.Song;

@Repository
public interface SongRepository extends MongoRepository<Song, String> {

} 