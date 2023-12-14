package com.hajela.authservice.exceptions;

public class UserStatusBlockedException extends RuntimeException {
    public UserStatusBlockedException(String status) {
        super(status);
    }
}
