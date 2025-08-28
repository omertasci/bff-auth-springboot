package com.example.bffauth.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.Map;

@Component
public class JwtUtil {

    @Value("${app.jwt.secret}")
    private String secret;

    @Value("${app.jwt.access-ms}")
    private long accessMs;

    @Value("${app.jwt.refresh-ms}")
    private long refreshMs;

    private Key signingKey;

    @PostConstruct
    public void init() {
        // secret'i bytes olarak kullanıyoruz; production'da key yönetimi yapın
        signingKey = Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String generateAccessToken(String username, String domain) {
        return Jwts.builder()
                .setSubject(username)
                .addClaims(Map.of("type", "access", "domain", domain))
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + accessMs))
                .signWith(signingKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateRefreshToken(String username, String domain) {
        return Jwts.builder()
                .setSubject(username)
                .addClaims(Map.of("type", "refresh", "domain", domain))
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + refreshMs))
                .signWith(signingKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(signingKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean validate(String token) {
        try {
            getClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException ex) {
            return false;
        }
    }

    public String extractUsername(String token) {
        return getClaims(token).getSubject();
    }

    public String extractType(String token) {
        Object t = getClaims(token).get("type");
        return t != null ? t.toString() : null;
    }

    public String extractDomain(String token) {
        Object d = getClaims(token).get("domain");
        return d != null ? d.toString() : null;
    }
}
