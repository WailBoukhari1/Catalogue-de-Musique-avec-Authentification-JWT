package com.youcode.musicalcatalogapi.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.youcode.musicalcatalogapi.dto.request.SongRequest;
import com.youcode.musicalcatalogapi.model.Song;
import com.youcode.musicalcatalogapi.service.SongService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin/songs")
@RequiredArgsConstructor
public class AdminSongController {
    private final SongService songService;

    @PostMapping
    public ResponseEntity<Song> createSong(@Valid @RequestBody SongRequest request) {
        return ResponseEntity.ok(songService.createSong(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Song> updateSong(
            @PathVariable String id,
            @Valid @RequestBody SongRequest request
    ) {
        return ResponseEntity.ok(songService.updateSong(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSong(@PathVariable String id) {
        songService.deleteSong(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Song> getSongById(@PathVariable String id) {
        return ResponseEntity.ok(songService.getSongById(id));
    }
} 