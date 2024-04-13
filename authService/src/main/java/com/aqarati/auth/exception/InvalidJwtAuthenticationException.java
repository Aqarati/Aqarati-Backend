package com.aqarati.exception;

public class InvalidJwtAuthenticationException extends Exception {
    public InvalidJwtAuthenticationException() {
    }

    public InvalidJwtAuthenticationException(String message) {
        super(message);
    }
}
