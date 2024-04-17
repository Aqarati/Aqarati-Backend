package com.aqarati.user.response;

import lombok.Data;
import org.springframework.validation.FieldError;

import java.util.ArrayList;
import java.util.List;

@Data
public class ValidationErrorResponse {
    private String message;
    private List<FieldError> errors= new ArrayList<>();
    private List<String> globalErrors= new ArrayList<>();

    public ValidationErrorResponse(String message) {
        this.message = message;
    }

    public void addError(String field, String message, Object rejectedValue) {
        errors.add(new FieldError(field, message, (String) rejectedValue));
    }

    public void addGlobalError(String message) {
        globalErrors.add(message);
    }
}
