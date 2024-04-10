package com.aqarati.property.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import com.aqarati.property.response.ExceptionResponse;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(InvalidJwtAuthenticationException.class)
    public ResponseEntity<ExceptionResponse> handleInvalidJwtAuthenticationException(InvalidJwtAuthenticationException ex) {
        final String message="The JWT may it expired,or The signature is invalid";
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ExceptionResponse(ex.getMessage(),message));
    }
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ExceptionResponse> handlePropertyNotFoundException(NotFoundException ex) {
        final String message="Property Exception";
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ExceptionResponse(ex.getMessage(),message));
    }
}
