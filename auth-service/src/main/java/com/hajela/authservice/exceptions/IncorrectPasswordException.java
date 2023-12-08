package com.hajela.authservice.exceptions;

public class IncorrectPasswordException extends RuntimeException {
    public IncorrectPasswordException(String email) {
        super(email);
    }
}
