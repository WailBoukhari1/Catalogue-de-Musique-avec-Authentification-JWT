package com.youcode.musicalcatalogapi.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.youcode.musicalcatalogapi.dto.request.SongRequest;
import com.youcode.musicalcatalogapi.model.Album;
import com.youcode.musicalcatalogapi.model.Song;
import com.youcode.musicalcatalogapi.repository.AlbumRepository;
import com.youcode.musicalcatalogapi.repository.SongRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SongService {
    private final SongRepository songRepository;
    private final AlbumRepository albumRepository;

    public Page<Song> getAllSongs(Pageable pageable) {
        return songRepository.findAll(pageable);
    }


    public Song createSong(SongRequest request) {
        Album album = albumRepository.findById(request.getAlbumId())
                .orElseThrow(() -> new RuntimeException("Album not found"));

        Song song = Song.builder()
                .titre(request.getTitre())
                .duree(request.getDuree())
                .trackNumber(request.getTrackNumber())
                .album(album)
                .build();

        return songRepository.save(song);
    }

    public Song updateSong(String id, SongRequest request) {
        Song song = songRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Song not found"));
        
        Album album = albumRepository.findById(request.getAlbumId())
                .orElseThrow(() -> new RuntimeException("Album not found"));

        song.setTitre(request.getTitre());
        song.setDuree(request.getDuree());
        song.setTrackNumber(request.getTrackNumber());
        song.setAlbum(album);

        return songRepository.save(song);
    }

    public void deleteSong(String id) {
        songRepository.deleteById(id);
    }

    public Song getSongById(String id) {
        return songRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Song not found"));
    }
} 