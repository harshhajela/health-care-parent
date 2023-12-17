package com.example.bookingservice.exceptions;

import com.example.bookingservice.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionResolver {

    @ExceptionHandler(value = BookingException.class)
    protected ResponseEntity<ErrorResponse> handleBookingException(BookingException ex) {
        return new ResponseEntity<>(ErrorResponse.builder()
                .code(ex.getCode())
                .message(ex.getMessage())
                .build(), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(value = InvalidAuthorizationHeaderException.class)
    protected ResponseEntity<ErrorResponse> handleInvalidAuthCredentialsException(InvalidAuthorizationHeaderException exception) {
        return new ResponseEntity<>(ErrorResponse.builder()
                .code("invalid.auth.header")
                .message("Authorization token either missing or expired!")
                .build(), HttpStatus.BAD_REQUEST);
    }
}
