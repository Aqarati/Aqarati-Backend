package com.aqarati.controller;

import com.aqarati.exception.RequestMissingInformation;
import com.aqarati.exception.UserAlreadyExists;
import com.aqarati.request.AuthRequest;
import com.aqarati.response.AuthResponse;
import com.aqarati.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@Validated
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> userRegister(@RequestBody @Valid AuthRequest authRequest) throws UserAlreadyExists, RequestMissingInformation {
        var token=authService.registerUser(authRequest);
        var res=AuthResponse.builder().
                message("User registration successful").
                token(token).
                tokenType("JWT bearer token").
                build();
        return new ResponseEntity<>(res, HttpStatus.CREATED);
    }
    @PostMapping("/signin")
    public ResponseEntity<AuthResponse> userLogin(@RequestBody AuthRequest authRequest) throws BadCredentialsException {
        var token=authService.loginUser(authRequest);
        var res=AuthResponse.builder().
                message("User login successful").
                token(token).
                tokenType("JWT bearer token").
                build();
        return new ResponseEntity<>(res,HttpStatus.OK);
    }

}
