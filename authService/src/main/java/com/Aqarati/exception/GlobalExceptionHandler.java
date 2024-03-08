package com.Aqarati.exception;

import com.Aqarati.response.ValidationErrorResponse;
import com.Aqarati.response.ExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(InvalidJwtAuthenticationException.class)
    public ResponseEntity<ExceptionResponse> handleInvalidJwtAuthenticationException(InvalidJwtAuthenticationException ex) {
        final String message="The JWT may it expired,or The signature is invalid";
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ExceptionResponse(ex.getMessage(),message));
    }
    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<String> handleNotFoundException(UsernameNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
    @ExceptionHandler(UserAlreadyExists.class)
    public ResponseEntity<ExceptionResponse> handleUserAlreadyExists(UserAlreadyExists ex) {
        final String message="The email address you provided is already registered. Please use a different email address.";
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ExceptionResponse(ex.getMessage(),message));
    }
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ExceptionResponse> handleBadCredentialsException(BadCredentialsException ex) {
        final String message="Invalid username or password. Please try again.";
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ExceptionResponse(ex.getMessage(),message));
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorResponse> handleValidationException(MethodArgumentNotValidException ex) {
        List<ObjectError> globalErrors = ex.getBindingResult().getGlobalErrors();
        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();

        ValidationErrorResponse errorResponse = new ValidationErrorResponse("Validation failed");

        for (FieldError fieldError : fieldErrors) {
            // Get the field name, error message, and rejected value
            String field = fieldError.getField();
            String message = fieldError.getDefaultMessage();
            Object rejectedValue = fieldError.getRejectedValue();

            // Add these details to the error response
            errorResponse.addError(field, message, rejectedValue);
        }

        for (ObjectError globalError : globalErrors) {
            // Handle global errors if needed
            String message = globalError.getDefaultMessage();
            errorResponse.addGlobalError(message);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }


}
