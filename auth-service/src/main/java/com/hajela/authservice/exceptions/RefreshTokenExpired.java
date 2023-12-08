package com.hajela.authservice.exceptions;

public class RefreshTokenExpired extends RuntimeException {
    public RefreshTokenExpired(String token) {
        super(String.format("Token: %s is expired", token));
    }
}
