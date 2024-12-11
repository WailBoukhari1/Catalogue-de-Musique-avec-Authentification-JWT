package com.youcode.musicalcatalogapi.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.youcode.musicalcatalogapi.model.Album;
import com.youcode.musicalcatalogapi.service.AlbumService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/user/albums")
@RequiredArgsConstructor
public class UserAlbumController {
    private final AlbumService albumService;

    @GetMapping
    public ResponseEntity<Page<Album>> getAllAlbums(Pageable pageable) {
        return ResponseEntity.ok(albumService.getAllAlbums(pageable));
    }

    @GetMapping("/search/titre")
    public ResponseEntity<Page<Album>> searchByTitle(
            @RequestParam String titre,
            Pageable pageable
    ) {
        return ResponseEntity.ok(albumService.searchByTitle(titre, pageable));
    }

    @GetMapping("/search/artiste")
    public ResponseEntity<Page<Album>> searchByArtist(
            @RequestParam String artiste,
            Pageable pageable
    ) {
        return ResponseEntity.ok(albumService.searchByArtist(artiste, pageable));
    }

    @GetMapping("/filter/annee")
    public ResponseEntity<Page<Album>> filterByYear(
            @RequestParam Integer annee,
            Pageable pageable
    ) {
        return ResponseEntity.ok(albumService.filterByYear(annee, pageable));
    }
} 