package com.hajela.apigateway.services;

import com.hajela.apigateway.exceptions.ExpiredTokenException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@Slf4j
@Service
public class JwtUtils {

    private String secret;
    private String expiration;
    private Key key;

    @Autowired
    public JwtUtils(@Value("${jwt.secret}") String secret, @Value("${jwt.expiration}") String expiration) {
        this.secret = secret;
        this.expiration = expiration;
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    public Claims getClaims(String token) {
        try {
            return Jwts
                    .parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            throw new ExpiredTokenException("Token expired!");
        }
    }

    public boolean isExpired(String token) {
        return getClaims(token).getExpiration().before(new Date());
    }

}
