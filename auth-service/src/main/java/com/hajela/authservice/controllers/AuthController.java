package com.hajela.authservice.controllers;

import com.hajela.authservice.dto.AuthRequest;
import com.hajela.authservice.dto.AuthResponse;
import com.hajela.authservice.dto.RefreshTokenRequest;
import com.hajela.authservice.dto.RegistrationRequest;
import com.hajela.authservice.services.AuthService;
import com.hajela.authservice.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final UserService userService;

    @PostMapping(value = "/register")
    public ResponseEntity<String> register(@RequestBody RegistrationRequest registrationRequest) {
        userService.createNewUser(registrationRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body("{\"message\": \"Registration successful\"}");
    }

    @PostMapping(value = "/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest authRequest) {
        return ResponseEntity.ok(authService.login(authRequest));
    }

    @PostMapping(value = "/refreshToken")
    public ResponseEntity<AuthResponse> refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        AuthResponse authResponse = authService.refreshToken(refreshTokenRequest);
        return new ResponseEntity<>(authResponse, HttpStatus.ACCEPTED);
    }

}
