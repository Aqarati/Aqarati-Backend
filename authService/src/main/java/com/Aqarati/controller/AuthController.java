package com.Aqarati.controller;

import com.Aqarati.exception.UserAlreadyExists;
import com.Aqarati.model.UserApp;
import com.Aqarati.request.AuthRequest;
import com.Aqarati.response.AuthResponse;
import com.Aqarati.service.AuthService;
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
    @GetMapping("/hel")
    public String test(){
        return "Hello world";
    }

    private final AuthService authService;
    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> userRegister(@RequestBody @Valid AuthRequest authRequest) throws UserAlreadyExists {
        var user=new UserApp(authRequest.getEmail().toLowerCase(), authRequest.getPassword());
        var token=authService.registerUser(user);
        var res=AuthResponse.builder().
                message("User registration successful").
                token(token).
                tokenType("JWT bearer token").
                build();
        return new ResponseEntity<AuthResponse>(res,HttpStatus.CREATED);
    }
    @PostMapping("/signin")
    public ResponseEntity<AuthResponse> userLogin(@RequestBody AuthRequest authRequest) throws BadCredentialsException {
        var token=authService.loginUser(authRequest);
        var res=AuthResponse.builder().
                message("User login successful").
                token(token).
                tokenType("JWT bearer token").
                build();
        return new ResponseEntity<AuthResponse>(res,HttpStatus.OK);
    }

//    @Autowired
//    JwtTokenUtil jwtTokenUtil;
//    @GetMapping("/validate")
//    private ResponseEntity<AuthResponse> getInfo(HttpServletRequest request) throws InvalidJwtAuthenticationException {
//        var token=jwtTokenUtil.resolveToken(request);
//        var valide=jwtTokenUtil.validateToken(token );
//        var authResponse=AuthResponse.builder().
//                message(Boolean.toString(valide)).
//                token(token).
//                tokenType("JWT bearer token").
//                build();
//        return ResponseEntity.ok().body(authResponse);
//    }
}
