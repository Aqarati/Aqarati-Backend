package com.aqarati.user.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import com.aqarati.user.exception.InvalidJwtAuthenticationException;

import java.util.Date;

@Service
@RequiredArgsConstructor
@PropertySource(value = {"classpath:application.properties"})
public class JwtTokenUtil {

    @Value("${jwt.secret.key}")
    private String secretKey;
    @Value("${jwt.expiration}")
    private long validityInMilliseconds;

    public String getEmail(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }

    public String resolveToken(HttpServletRequest req) throws InvalidJwtAuthenticationException {
        String bearerToken = req.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7, bearerToken.length());
        }
        throw new InvalidJwtAuthenticationException("Can`t Resolve Token");
    }

    public boolean validateToken(String token)throws InvalidJwtAuthenticationException {

            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            if (claims.getBody().getExpiration().before(new Date())) {
                throw new InvalidJwtAuthenticationException("jwt expired");
            }
            return true;
    }
}
