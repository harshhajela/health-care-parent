package com.hajela.authservice.exceptions;

import com.hajela.authservice.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandling {

    @ExceptionHandler(value = ResetForgotPasswordException.class)
    protected ResponseEntity<ErrorResponse> handleResetForgotPasswordException(ResetForgotPasswordException ex) {
        return new ResponseEntity<>(ErrorResponse.builder()
                .code("reset.password.exception")
                .message(ex.getMessage())
                .build(), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(value = ForgotPasswordException.class)
    protected ResponseEntity<ErrorResponse> handleForgotPasswordException(ForgotPasswordException ex) {
        return new ResponseEntity<>(ErrorResponse.builder()
                .code("forgot.password.email.not.found")
                .message(ex.getMessage())
                .build(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = ActivateAccountException.class)
    protected ResponseEntity<ErrorResponse> handleActivateAccountException(ActivateAccountException ex) {
        ErrorResponse errorResponse;
        if (ex.getMessage().contains("expired")) {
            errorResponse = ErrorResponse.builder()
                    .code("activation.link.expired")
                    .message(ex.getMessage())
                    .build();
        } else if (ex.getMessage().contains("already used")) {
            errorResponse = ErrorResponse.builder()
                    .code("activation.link.already.used")
                    .message(ex.getMessage())
                    .build();
        } else {
            errorResponse = ErrorResponse.builder()
                    .code("activation.link.error")
                    .message(ex.getMessage())
                    .build();
        }
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = RefreshTokenExpired.class)
    protected ResponseEntity<ErrorResponse> handleRefreshTokenExpired(RefreshTokenExpired ex) {
        return new ResponseEntity<>(ErrorResponse.builder()
                .code("refreshToken.expired")
                .message(ex.getMessage())
                .build(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(value = RefreshTokenNotFound.class)
    protected ResponseEntity<ErrorResponse> handleRefreshTokenNotFound(RefreshTokenNotFound ex) {
        return new ResponseEntity<>(ErrorResponse.builder()
                .code("refreshToken.not.found")
                .message(ex.getMessage())
                .build(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(value = InvalidAuthCredentialsException.class)
    protected ResponseEntity<ErrorResponse> handleInvalidAuthCredentialsException(InvalidAuthCredentialsException exception) {
        return new ResponseEntity<>(ErrorResponse.builder()
                .code("invalid.credentials")
                .message("Please check email/password provided")
                .build(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = ClientBadRequestException.class)
    protected ResponseEntity<ErrorResponse> handleClientBadRequestException(ClientBadRequestException ex) {
        return new ResponseEntity<>(ErrorResponse.builder()
                .code(ex.getCode())
                .message(ex.getMessage())
                .build(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = InvalidEmailException.class)
    protected ResponseEntity<ErrorResponse> handleInvalidEmailException(InvalidEmailException exception) {
        return new ResponseEntity<>(ErrorResponse.builder()
                .code("email.not.found")
                .message("No user found with email=" + exception.getMessage())
                .build(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = IncorrectPasswordException.class)
    protected ResponseEntity<ErrorResponse> handleIncorrectPasswordException(IncorrectPasswordException exception) {
        return new ResponseEntity<>(ErrorResponse.builder()
                .code("incorrect.password")
                .message("Incorrect Password provided for email=" + exception.getMessage())
                .build(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = UserStatusBlockedException.class)
    protected ResponseEntity<ErrorResponse> handleUserStatusBlockedException(UserStatusBlockedException ex) {
        return new ResponseEntity<>(ErrorResponse.builder()
                .code("user.status")
                .message(String.format("User status is %s. Please contact customer support", ex.getMessage()))
                .build(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = UserAlreadyExists.class)
    protected ResponseEntity<ErrorResponse> handleUserAlreadyExists(UserAlreadyExists exception) {
        return new ResponseEntity<>(ErrorResponse.builder()
                .code("user.exists")
                .message(exception.getMessage())
                .build(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = UserEmailNotFoundException.class)
    protected ResponseEntity<ErrorResponse> handleUserEmailNotFoundException(UserEmailNotFoundException exception) {
        return new ResponseEntity<>(ErrorResponse.builder()
                .code("email.not.found")
                .message(exception.getMessage())
                .build(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = InvalidRoleException.class)
    protected ResponseEntity<ErrorResponse> handleInvalidRoleException(InvalidRoleException exception) {
        return new ResponseEntity<>(ErrorResponse.builder()
                .code("invalid.role")
                .message(String.format("Role %s is not supported", exception.getMessage()))
                .build(), HttpStatus.BAD_REQUEST);
    }
}
