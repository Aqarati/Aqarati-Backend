package com.Aqarati.service;

import com.Aqarati.exception.InvalidJwtAuthenticationException;
import com.Aqarati.exception.UserAlreadyExists;
import com.Aqarati.repository.UserRepository;
import com.Aqarati.model.UserApp;
import com.Aqarati.request.AuthRequest;
import com.Aqarati.security.JwtTokenUtil;
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

    public String registerUser(UserApp user) throws UserAlreadyExists {

        var x=userRepository.findByEmail(user.getEmail().toLowerCase());
        var x1=userRepository.findByUname(user.getUname().toLowerCase());
        System.out.println(user);
        System.out.println("x1  "+x1);
        if(x.isPresent()){
            throw new UserAlreadyExists("Email address already exists");
        }
        if(x1.isPresent()){
            throw new UserAlreadyExists("username already exists");
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
