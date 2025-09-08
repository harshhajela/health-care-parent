package com.hajela.authservice.controllers;

import com.hajela.authservice.dto.*;
import com.hajela.authservice.services.AuthService;
import com.hajela.authservice.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Authentication and user management operations")
public class AuthController {

    private final AuthService authService;
    private final UserService userService;

    @Operation(summary = "Register new user", description = "Register a new user account with customer or provider role")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "User registered successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid registration data"),
        @ApiResponse(responseCode = "409", description = "User already exists")
    })
    @PostMapping(value = "/register")
    public ResponseEntity<Void> register(
        @Parameter(description = "User registration details", required = true)
        @Validated @RequestBody RegistrationRequest registrationRequest) {
        userService.createNewUser(registrationRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "User login", description = "Authenticate user and return JWT tokens")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Login successful", 
            content = @Content(schema = @Schema(implementation = AuthResponse.class))),
        @ApiResponse(responseCode = "401", description = "Invalid credentials"),
        @ApiResponse(responseCode = "403", description = "Account not activated")
    })
    @PostMapping(value = "/login")
    public ResponseEntity<AuthResponse> login(
        @Parameter(description = "User credentials", required = true)
        @RequestBody AuthRequest authRequest) {
        return ResponseEntity.ok(authService.login(authRequest));
    }

    @Operation(summary = "User logout", description = "Logout user and invalidate refresh token")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Logout successful"),
        @ApiResponse(responseCode = "401", description = "Invalid token")
    })
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping(value = "/logout")
    public ResponseEntity<Void> logout(
        @Parameter(description = "JWT Bearer token", required = true)
        @RequestHeader(name = "Authorization") String authorizationHeader) {
        if (authorizationHeader != null) {
            authService.logout(authorizationHeader);
        }
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Refresh JWT token", description = "Get new access token using refresh token")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "202", description = "Token refreshed successfully", 
            content = @Content(schema = @Schema(implementation = AuthResponse.class))),
        @ApiResponse(responseCode = "401", description = "Invalid refresh token")
    })
    @PostMapping(value = "/refreshToken")
    public ResponseEntity<AuthResponse> refreshToken(
        @Parameter(description = "Refresh token request", required = true)
        @RequestBody RefreshTokenRequest refreshTokenRequest) {
        AuthResponse authResponse = authService.refreshToken(refreshTokenRequest);
        return new ResponseEntity<>(authResponse, HttpStatus.ACCEPTED);
    }

    @Operation(summary = "Activate user account", description = "Activate user account using activation token")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Account activated successfully", 
            content = @Content(schema = @Schema(implementation = AuthResponse.class))),
        @ApiResponse(responseCode = "400", description = "Invalid activation token")
    })
    @PutMapping(value = "/activate")
    public ResponseEntity<AuthResponse> activateAccount(
        @Parameter(description = "Account activation details", required = true)
        @RequestBody ActivateAccountDto activateAccountDto) {
        return new ResponseEntity<>(authService.activateAccount(activateAccountDto), HttpStatus.OK);
    }

    @Operation(summary = "Initiate password reset", description = "Send password reset email to user")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Password reset email sent"),
        @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PostMapping(value = "/forgotPassword")
    public ResponseEntity<Void> forgotPassword(
        @Parameter(description = "Email for password reset", required = true)
        @RequestBody ForgotPasswordDto forgotPasswordDto) {
        authService.forgotPassword(forgotPasswordDto);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Reset user password", description = "Reset user password using reset token")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Password reset successful"),
        @ApiResponse(responseCode = "400", description = "Invalid reset token or password")
    })
    @PostMapping(value = "/resetPassword")
    public ResponseEntity<Void> resetPassword(
        @Parameter(description = "Password reset details", required = true)
        @RequestBody ResetPasswordDto resetPasswordDto) {
        authService.resetUserPassword(resetPasswordDto);
        return ResponseEntity.ok().build();
    }
}
