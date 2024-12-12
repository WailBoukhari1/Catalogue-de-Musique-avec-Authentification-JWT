package com.youcode.musicalcatalogapi.dto.request;

import jakarta.validation.constraints.Max;
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
public class AlbumRequest {
    @NotBlank(message = "Title is required")
    private String titre;
    
    @NotBlank(message = "Artist is required")
    private String artiste;
    
    @NotNull(message = "Year is required")
    @Min(value = 1900, message = "Year must be greater than 1900")
    @Max(value = 2024, message = "Year cannot be in the future")
    private Integer annee;
} 