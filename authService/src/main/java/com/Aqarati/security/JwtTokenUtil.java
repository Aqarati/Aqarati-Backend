package com.Aqarati.security;

import com.Aqarati.exception.InvalidJwtAuthenticationException;
import com.Aqarati.model.UserApp;
import io.jsonwebtoken.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;

@Service
@RequiredArgsConstructor
@PropertySource(value = {"classpath:application.properties"})
public class JwtTokenUtil {
    private final UserDetailsService userDetailsService;

    @Value("${jwt.secret.key}")
    private String secretKey;
    @Value("${jwt.expiration}")
    private long validityInMilliseconds;

    public String createToken(UserApp userApp) {
        Claims claims = Jwts.claims().setSubject(userApp.getEmail());
        claims.put("uid",userApp.getId());
        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("typ", "JWT");
        System.out.println(claims.toString());
        return Jwts.builder()
                .setHeader(hashMap)
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public Authentication getAuthentication(String token) {
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(getEmail(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "");
    }

    public String getEmail(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody().getSubject();
    }
    public String getUserId(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody()
                .get("uid", String.class);
    }


    public String resolveToken(HttpServletRequest req) throws InvalidJwtAuthenticationException {
        String bearerToken = req.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7, bearerToken.length());
        }
        throw new InvalidJwtAuthenticationException("Can`t Reslove Token");
    }


    public boolean validateToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            if (claims.getBody().getExpiration().before(new Date())) {
                return false;
            }
            return true;
        } catch (Exception ex) {
            System.out.println(ex);
            return false;
        }
    }
}
