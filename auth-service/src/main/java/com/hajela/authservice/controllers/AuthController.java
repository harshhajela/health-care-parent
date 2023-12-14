package com.hajela.authservice.controllers;

import com.hajela.authservice.dto.*;
import com.hajela.authservice.services.AuthService;
import com.hajela.authservice.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final UserService userService;

    @PostMapping(value = "/register")
    public ResponseEntity<Void> register(@RequestBody RegistrationRequest registrationRequest) {
        userService.createNewUser(registrationRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
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

    @PutMapping(value = "/activate")
    public ResponseEntity<AuthResponse> activateAccount(@RequestBody ActivateAccountDto activateAccountDto) {
        return new ResponseEntity<>(authService.activateAccount(activateAccountDto), HttpStatus.OK);
    }

    @PostMapping(value = "/forgotPassword")
    public ResponseEntity<Void> forgotPassword(@RequestBody ForgotPasswordDto forgotPasswordDto) {
        authService.forgotPassword(forgotPasswordDto);
        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "/resetPassword")
    public ResponseEntity<Void> resetPassword(@RequestBody ResetPasswordDto resetPasswordDto) {
        authService.resetUserPassword(resetPasswordDto);
        return ResponseEntity.ok().build();
    }
}
