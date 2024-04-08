package com.aqarati.document.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import com.aqarati.document.response.ExceptionResponse;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleException(Exception ex) {
        final String message="Exception occurred";
        return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ExceptionResponse(ex.getMessage(),message));
    }
    @ExceptionHandler(InvalidJwtAuthenticationException.class)
    public ResponseEntity<ExceptionResponse> handleInvalidJwtAuthenticationException(InvalidJwtAuthenticationException ex) {
        final String message="The JWT may it expired,or The signature is invalid";
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ExceptionResponse(ex.getMessage(),message));
    }
    @ExceptionHandler(PropertyNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handlePropertyNotFoundException(PropertyNotFoundException ex) {
        final String message="Property Exception";
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ExceptionResponse(ex.getMessage(),message));
    }
    @ExceptionHandler(UnAuthorizedAccessException.class)
    public ResponseEntity<ExceptionResponse> handleUnAuthorizedAccessException(UnAuthorizedAccessException ex) {
        final String message="unauthorized Access ";
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ExceptionResponse(ex.getMessage(),message));
    }
}
