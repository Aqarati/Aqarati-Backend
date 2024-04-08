package com.aqarati.document.exception;

public class UnAuthorizedAccessException extends Exception {
    public UnAuthorizedAccessException() {
        super();
    }

    public UnAuthorizedAccessException(String message) {
        super(message);
    }
}
