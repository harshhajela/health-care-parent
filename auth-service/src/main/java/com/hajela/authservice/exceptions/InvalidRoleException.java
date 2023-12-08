package com.hajela.authservice.exceptions;

public class InvalidRoleException extends RuntimeException {

    public InvalidRoleException(String roleName) {
        super(roleName);
    }
}
