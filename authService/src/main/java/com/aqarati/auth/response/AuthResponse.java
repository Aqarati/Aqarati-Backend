package com.aqarati.auth.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor@NoArgsConstructor@Builder@Data
public class AuthResponse {
    private String message;
    private String token;
    private String tokenType;
}
