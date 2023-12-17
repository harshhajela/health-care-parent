package com.example.bookingservice.exceptions;

public class InvalidAuthorizationHeaderException extends RuntimeException {

    public InvalidAuthorizationHeaderException(){
        super("Invalid Authorization Header");
    }
}
