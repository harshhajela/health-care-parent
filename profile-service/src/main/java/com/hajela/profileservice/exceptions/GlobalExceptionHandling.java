package com.hajela.profileservice.exceptions;

import com.hajela.profileservice.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandling {

    @ExceptionHandler(value = {InvalidAuthorizationHeaderException.class, MissingRequestHeaderException.class})
    protected ResponseEntity<ErrorResponse> handleInvalidAuthCredentialsException(Exception exception) {
        String errorCode;
        String errorMessage;

        if (exception instanceof InvalidAuthorizationHeaderException) {
            errorCode = "invalid.auth.header";
            errorMessage = "Invalid authorization token!";
        } else if (exception instanceof MissingRequestHeaderException) {
            errorCode = "missing.request.header";
            errorMessage = "Required request header is missing!";
        } else {
            // Default values for other exceptions
            errorCode = "unknown.error";
            errorMessage = "An unexpected error occurred!";
        }

        return new ResponseEntity<>(ErrorResponse.builder()
                .code(errorCode)
                .message(errorMessage)
                .build(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = ProfileNotFoundException.class)
    protected ResponseEntity<Void> handleProfileNotFoundException(ProfileNotFoundException exception) {
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult()
                .getAllErrors()
                .stream()
                .map(GlobalExceptionHandling::apply)
                .toList();
        return new ResponseEntity<>(ErrorResponse.builder()
                .code("invalid.request")
                .message("Please check errors for more details!")
                .errors(errors)
                .build(), HttpStatus.BAD_REQUEST);
    }

    private static String apply(ObjectError error) {
        if (error instanceof FieldError fieldError) {
            return fieldError.getField() + ": " + error.getDefaultMessage();
        } else {
            return error.getDefaultMessage();
        }
    }
}
