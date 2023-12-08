package com.hajela.authservice.exceptions;

public class UserAlreadyExists extends RuntimeException {

    public UserAlreadyExists(String email) { super("User exists with email: " + email); }
}
