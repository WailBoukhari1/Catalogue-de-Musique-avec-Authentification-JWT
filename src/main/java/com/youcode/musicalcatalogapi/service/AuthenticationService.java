package com.youcode.musicalcatalogapi.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

import com.youcode.musicalcatalogapi.dto.response.AuthenticationResponse;
import com.youcode.musicalcatalogapi.dto.request.AuthenticationRequest;
import com.youcode.musicalcatalogapi.dto.request.RegisterRequest;
import com.youcode.musicalcatalogapi.model.Role;
import com.youcode.musicalcatalogapi.model.User;
import com.youcode.musicalcatalogapi.repository.UserRepository;
import com.youcode.musicalcatalogapi.security.JwtService;
import com.youcode.musicalcatalogapi.exception.UserAlreadyExistsException;
import com.youcode.musicalcatalogapi.exception.InvalidCredentialsException;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {
        if (userRepository.existsByLogin(request.getLogin())) {
            throw new UserAlreadyExistsException("User with this email already exists");
        }

        var user = User.builder()
                .login(request.getLogin())
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(Collections.singletonList(Role.ROLE_USER))
                .active(true)
                .build();

        userRepository.save(user);
        var jwtToken = jwtService.generateToken(user);
        
        log.info("New user registered: {}", request.getLogin());
        
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .message("User registered successfully")
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getLogin(),
                            request.getPassword()
                    )
            );

            var user = userRepository.findByLogin(request.getLogin())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));
            
            var jwtToken = jwtService.generateToken(user);
            
            log.info("User authenticated successfully: {}", request.getLogin());
            
            return AuthenticationResponse.builder()
                    .token(jwtToken)
                    .message("Authentication successful")
                    .build();
                    
        } catch (AuthenticationException e) {
            log.error("Authentication failed for user: {}", request.getLogin());
            throw new InvalidCredentialsException("Invalid username or password");
        }
    }
} 