package com.hajela.authservice.exceptions;

public class UserEmailNotFoundException extends RuntimeException {
    public UserEmailNotFoundException(String email) {
        super(String.format("User with email %s not found",email));
    }
}
