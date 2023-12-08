package com.hajela.careprovider.exceptions;

import com.hajela.careprovider.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandling {

    @ExceptionHandler(value = InvalidAuthorizationHeaderException.class)
    protected ResponseEntity<ErrorResponse> handleInvalidAuthCredentialsException(InvalidAuthorizationHeaderException exception) {
        return new ResponseEntity<>(ErrorResponse.builder()
                .code("invalid.auth.header")
                .message("Authorization token either missing or expired!")
                .build(), HttpStatus.BAD_REQUEST);
    }

}
