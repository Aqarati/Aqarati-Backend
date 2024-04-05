package com.aqarati.service;

import com.aqarati.exception.InvalidJwtAuthenticationException;
import com.aqarati.exception.RequestMissingInformation;
import com.aqarati.exception.UserAlreadyExists;
import com.aqarati.repository.UserRepository;
import com.aqarati.model.UserApp;
import com.aqarati.request.AuthRequest;
import com.aqarati.security.JwtTokenUtil;
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
        var user=new UserApp(authRequest.getEmail().toLowerCase(), authRequest.getPassword(),authRequest.getUsername().toLowerCase(),authRequest.getPhoneNumber());
        if(userRepository.findByEmail(user.getEmail().toLowerCase()).isPresent()){
            throw new UserAlreadyExists("Email address already exists");
        }
        if(userRepository.findByUname(user.getUname().toLowerCase()).isPresent()){
            throw new UserAlreadyExists("username already exists");
        }
        if(userRepository.findByPhoneNumber(user.getUname().toLowerCase()).isPresent()){
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
