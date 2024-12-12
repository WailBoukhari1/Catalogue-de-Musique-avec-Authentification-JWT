package com.youcode.musicalcatalogapi.controller;

import com.youcode.musicalcatalogapi.dto.request.AuthenticationRequest;
import com.youcode.musicalcatalogapi.dto.request.RegisterRequest;
import com.youcode.musicalcatalogapi.dto.response.AuthenticationResponse;
import com.youcode.musicalcatalogapi.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Validated
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final Environment environment;

    @PostMapping("/register")
    public ResponseEntity<?> register(
            @Valid @RequestBody RegisterRequest request
    ) {
        if (isDevProfile()) {
            return ResponseEntity.ok(new AuthenticationResponse(
                "dev-token",
                "Development mode: Authentication is disabled. All endpoints are accessible."
            ));
        }
        return ResponseEntity.ok(authenticationService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticate(
            @Valid @RequestBody AuthenticationRequest request
    ) {
        if (isDevProfile()) {
            return ResponseEntity.ok(new AuthenticationResponse(
                "dev-token",
                "Development mode: Authentication is disabled. All endpoints are accessible."
            ));
        }
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }

    private boolean isDevProfile() {
        String[] activeProfiles = environment.getActiveProfiles();
        for (String profile : activeProfiles) {
            if ("dev".equals(profile)) {
                return true;
            }
        }
        return false;
    }
}