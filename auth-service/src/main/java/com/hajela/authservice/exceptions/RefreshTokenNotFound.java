package com.hajela.authservice.exceptions;

public class RefreshTokenNotFound extends RuntimeException {
    public RefreshTokenNotFound(String token) {
        super(String.format("Token: %s not found in database!", token));
    }
}
