package com.youcode.musicalcatalogapi.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.youcode.musicalcatalogapi.model.Song;
import com.youcode.musicalcatalogapi.service.SongService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/user/songs")
@RequiredArgsConstructor
public class UserSongController {
    private final SongService songService;

    @GetMapping
    public ResponseEntity<Page<Song>> getAllSongs(Pageable pageable) {
        return ResponseEntity.ok(songService.getAllSongs(pageable));
    }

    @GetMapping("/search")
    public ResponseEntity<Page<Song>> searchByTitle(
            @RequestParam String titre,
            Pageable pageable
    ) {
        return ResponseEntity.ok(songService.searchByTitle(titre, pageable));
    }

    @GetMapping("/album/{albumId}")
    public ResponseEntity<Page<Song>> getSongsByAlbum(
            @PathVariable String albumId,
            Pageable pageable
    ) {
        return ResponseEntity.ok(songService.getSongsByAlbum(albumId, pageable));
    }
} 