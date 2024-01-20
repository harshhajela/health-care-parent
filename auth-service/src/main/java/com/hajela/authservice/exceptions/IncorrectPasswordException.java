package com.hajela.authservice.exceptions;

public class IncorrectPasswordException extends RuntimeException {
    public IncorrectPasswordException(String email) {
        super("Incorrect Password provided for email=" + email);
    }

    public IncorrectPasswordException() {
        super("Incorrect password provided!");
    }
}
