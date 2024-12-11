package com.youcode.musicalcatalogapi.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationRequest {
    @NotBlank(message = "Login is required")
    @Pattern(regexp = "^[a-zA-Z0-9._-]+$", message = "Login can only contain letters, numbers, dots, underscores and hyphens")
    @Size(min = 3, max = 50, message = "Login must be between 3 and 50 characters")
    private String login;
    
    @NotBlank(message = "Password is required")
    private String password;
}