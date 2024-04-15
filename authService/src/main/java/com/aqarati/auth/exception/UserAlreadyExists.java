package com.aqarati.auth.exception;

public class UserAlreadyExists extends Exception {
    public UserAlreadyExists() {
        super();
    }

    public UserAlreadyExists(String message) {
        super(message);
    }
}
