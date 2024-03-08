package com.Aqarati.controller;

import com.Aqarati.exception.InvalidJwtAuthenticationException;
import com.Aqarati.security.JwtTokenUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
public class TestController {
    @Autowired
    JwtTokenUtil jwtTokenUtil;
    @GetMapping
    public String getHello(HttpServletRequest request) throws InvalidJwtAuthenticationException {
        return "Hello "+jwtTokenUtil.getEmail(jwtTokenUtil.resolveToken(request))+"    "+ jwtTokenUtil.getUserId(jwtTokenUtil.resolveToken(request));
    }

}
