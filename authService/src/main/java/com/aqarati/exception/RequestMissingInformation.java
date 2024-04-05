package com.aqarati.exception;

public class RequestMissingInformation extends Exception{
    public RequestMissingInformation() {
        super();
    }

    public RequestMissingInformation(String message) {
        super(message);
    }
}
