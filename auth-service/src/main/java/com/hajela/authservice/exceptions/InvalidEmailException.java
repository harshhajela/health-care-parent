package com.hajela.authservice.exceptions;

public class InvalidEmailException extends RuntimeException {

    public InvalidEmailException(String email) {
        super(email);
    }
}
