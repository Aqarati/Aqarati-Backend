package com.aqarati.user.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor@NoArgsConstructor@Builder@Data
public class ExceptionResponse {
    private String error;
    private String message;

}
