package com.Aqarati.exception;

public class UserAlreadyExists extends Exception {
    public UserAlreadyExists() {
        super();
    }

    public UserAlreadyExists(String message) {
        super(message);
    }
}
