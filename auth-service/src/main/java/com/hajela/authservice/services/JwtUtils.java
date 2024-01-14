package com.hajela.authservice.services;

import com.hajela.authservice.entities.UserEntity;
import com.hajela.authservice.exceptions.InvalidAuthorizationHeaderException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

@Service
public class JwtUtils {

    private String secret;

    private Long expiration;

    private Key key;

    @Autowired
    public JwtUtils(@Value("${jwt.secret}") String secret, @Value("${jwt.expiration}") String expiration) {
        this.secret = secret;
        this.expiration = Long.parseLong(expiration);
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    public Claims getClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public Date getExpirationDate(String token) {
        return getClaims(token).getExpiration();
    }

    public String generate(UserEntity userEntity, String tokenType) {
        Map<String, String> claims = Map.of(
                "email", userEntity.getEmail(),
                "role", userEntity.getRole().getName().getRoleName(),
                "id", userEntity.getUserId().toString());
        return buildToken(claims, tokenType);
    }

    private String buildToken(Map<String, String> claims, String tokenType) {
//        long expMillis = "ACCESS".equalsIgnoreCase(tokenType)
//        ? Long.parseLong(expiration) * 1000
//        : Long.parseLong(expiration) * 1000 * 5;
//        long expMillis = 1 * 60 * 1000;

        final Date now = new Date();
        final Date exp = new Date(now.getTime() + this.expiration);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(claims.get("email"))
                .setIssuedAt(now)
                .setExpiration(exp)
                .signWith(key)
                .compact();
    }

    private boolean isExpired(String token) {
        return getExpirationDate(token).before(new Date());
    }

    public String getEmailFromHeader(String authorizationHeader) {
        String token = extractJwtToken(authorizationHeader).orElse(null);
        if (token == null) return null;
        try {
            return getClaims(token).getSubject();
        } catch (ExpiredJwtException ex) {
            return null;
        }
    }

    private Optional<String> extractJwtToken(String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String extractedToken = authorizationHeader.substring(7);
            return Optional.of(extractedToken);
        }
        return Optional.empty();
    }
}
