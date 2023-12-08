package com.hajela.careprovider.exceptions;

public class InvalidAuthorizationHeaderException extends RuntimeException{

    public InvalidAuthorizationHeaderException(){
        super("Invalid Authorization Header");
    }
}
