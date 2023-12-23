package com.hajela.profileservice.service;

import com.hajela.profileservice.exceptions.InvalidAuthorizationHeaderException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@Slf4j
@Service
public class JwtUtils {

    private String secret;
    private Key key;

    public JwtUtils(@Value("${jwt.secret}") String secret) {
        this.secret = secret;
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    private Claims getClaims(String token) {
        try {
            return Jwts
                    .parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            log.error("Expired token received!");
            throw new InvalidAuthorizationHeaderException();
        }
    }

    public String getEmailFromHeader(String authorizationHeader) {
        String token = extractJwtToken(authorizationHeader);
        String subject = getClaims(token).getSubject();
        log.info("subject={}", subject);
        return subject;
    }

    public boolean isExpired(String token) {
        return getClaims(token).getExpiration().before(new Date());
    }

    private String extractJwtToken(String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String extractedToken = authorizationHeader.substring(7);
            if (!isExpired(extractedToken)) return extractedToken;
        }
        throw new InvalidAuthorizationHeaderException();
    }
}
