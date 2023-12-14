package com.hajela.authservice.exceptions;

public class UserIdNotFoundException extends RuntimeException {

    public UserIdNotFoundException(Long userId) {
        super("User not found with Id: " + userId);
    }
}
