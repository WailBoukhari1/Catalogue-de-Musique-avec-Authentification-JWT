package com.youcode.musicalcatalogapi.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.youcode.musicalcatalogapi.dto.request.AlbumRequest;
import com.youcode.musicalcatalogapi.model.Album;
import com.youcode.musicalcatalogapi.service.AlbumService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin/albums")
@RequiredArgsConstructor
public class AdminAlbumController {
    private final AlbumService albumService;

    @PostMapping
    public ResponseEntity<Album> createAlbum(@Valid @RequestBody AlbumRequest request) {
        return ResponseEntity.ok(albumService.createAlbum(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Album> updateAlbum(
            @PathVariable String id,
            @Valid @RequestBody AlbumRequest request
    ) {
        return ResponseEntity.ok(albumService.updateAlbum(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAlbum(@PathVariable String id) {
        albumService.deleteAlbum(id);
        return ResponseEntity.noContent().build();
    }
} 