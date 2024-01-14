package com.hajela.authservice.exceptions;

public class InvalidAuthorizationHeaderException extends RuntimeException {

    public InvalidAuthorizationHeaderException(){
        super("Invalid Authorization Header");
    }
}
