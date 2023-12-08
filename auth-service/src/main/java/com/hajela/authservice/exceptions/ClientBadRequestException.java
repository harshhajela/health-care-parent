package com.hajela.authservice.exceptions;

import lombok.Getter;

@Getter
public class ClientBadRequestException extends RuntimeException {

    private final String code;
    private final String message;

    public ClientBadRequestException(String code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

}
