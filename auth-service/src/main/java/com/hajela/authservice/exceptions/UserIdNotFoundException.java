package com.hajela.authservice.exceptions;

public class UserIdNotFoundException extends RuntimeException {

    public UserIdNotFoundException(Integer userId) {
        super("User not found with Id: " + userId);
    }
}
