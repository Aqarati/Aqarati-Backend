package com.aqarati.auth.exception;

public class RequestMissingInformation extends Exception{
    public RequestMissingInformation() {


    }

    public RequestMissingInformation(String message) {
        super(message);
    }
}
