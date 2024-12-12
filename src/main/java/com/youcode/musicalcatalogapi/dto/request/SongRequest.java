package com.youcode.musicalcatalogapi.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SongRequest {
    @NotBlank(message = "Title is required")
    private String titre;
    
    @NotNull(message = "Duration is required")
    @Min(value = 1, message = "Duration must be greater than 0")
    private Integer duree;
    
    @NotNull(message = "Track number is required")
    @Min(value = 1, message = "Track number must be greater than 0")
    private Integer trackNumber;
    
    @NotBlank(message = "Album ID is required")
    private String albumId;
} 