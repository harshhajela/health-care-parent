package com.hajela.profileservice.exceptions;

public class InvalidAuthorizationHeaderException extends RuntimeException{

    public InvalidAuthorizationHeaderException(){
        super("Invalid Authorization Header");
    }
}
