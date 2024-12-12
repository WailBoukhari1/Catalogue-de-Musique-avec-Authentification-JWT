package com.youcode.musicalcatalogapi.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.youcode.musicalcatalogapi.dto.request.AlbumRequest;
import com.youcode.musicalcatalogapi.model.Album;
import com.youcode.musicalcatalogapi.repository.AlbumRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AlbumService {
    private final AlbumRepository albumRepository;

    public Page<Album> getAllAlbums(Pageable pageable) {
        return albumRepository.findAll(pageable);
    }

    public Page<Album> searchByTitle(String titre, Pageable pageable) {
        return albumRepository.findByTitreContainingIgnoreCase(titre, pageable);
    }

    public Page<Album> searchByArtist(String artiste, Pageable pageable) {
        return albumRepository.findByArtisteContainingIgnoreCase(artiste, pageable);
    }

    public Page<Album> filterByYear(Integer annee, Pageable pageable) {
        return albumRepository.findByAnnee(annee, pageable);
    }

    public Album createAlbum(AlbumRequest request) {
        Album album = Album.builder()
                .titre(request.getTitre())
                .artiste(request.getArtiste())
                .annee(request.getAnnee())
                .build();
        return albumRepository.save(album);
    }

    public Album updateAlbum(String id, AlbumRequest request) {
        Album album = albumRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Album not found"));
        
        album.setTitre(request.getTitre());
        album.setArtiste(request.getArtiste());
        album.setAnnee(request.getAnnee());
        
        return albumRepository.save(album);
    }

    public void deleteAlbum(String id) {
        albumRepository.deleteById(id);
    }

    public Album getAlbumById(String id) {
        return albumRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Album not found"));
    }
} 