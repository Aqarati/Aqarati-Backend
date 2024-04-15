package com.aqarati.auth.service;

import com.aqarati.auth.exception.InvalidJwtAuthenticationException;
import com.aqarati.auth.exception.RequestMissingInformation;
import com.aqarati.auth.exception.UserAlreadyExists;
import com.aqarati.auth.repository.UserRepository;
import com.aqarati.auth.model.UserApp;
import com.aqarati.auth.request.AuthRequest;
import com.aqarati.auth.security.JwtTokenUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;

    public String resolveToken(HttpServletRequest request) throws InvalidJwtAuthenticationException {
        return jwtTokenUtil.resolveToken(request);
    }

    public boolean validate(HttpServletRequest req) throws InvalidJwtAuthenticationException {
        String token = jwtTokenUtil.resolveToken(req);
        return jwtTokenUtil.validateToken(token);
    }

    public String registerUser(AuthRequest authRequest ) throws UserAlreadyExists,RequestMissingInformation {
        var user= UserApp.builder().
                        email(authRequest.getEmail().toLowerCase()).
                        password(authRequest.getPassword()).
                        uname(authRequest.getUsername()).
                        phoneNumber(authRequest.getPhoneNumber()).
                        build();

        if(userRepository.findByEmail(user.getEmail().toLowerCase()).isPresent()){
            throw new UserAlreadyExists("Email address already exists");
        }
        if(userRepository.findByUname(user.getUname().toLowerCase()).isPresent()){
            throw new UserAlreadyExists("username already exists");
        }
        if(userRepository.findByPhoneNumber(user.getPhoneNumber().toLowerCase()).isPresent()){
            throw new UserAlreadyExists("phoneNumber already exists");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return jwtTokenUtil.createToken(user);
    }

    public String loginUser(AuthRequest authRequest) throws BadCredentialsException {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getEmail().toLowerCase(), authRequest.getPassword()));
        var user=userRepository.findByEmail(authRequest.getEmail().toLowerCase()).get();
        return jwtTokenUtil.createToken(user);
    }
}
