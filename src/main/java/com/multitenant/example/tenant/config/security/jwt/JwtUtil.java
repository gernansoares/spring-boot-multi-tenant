package com.multitenant.example.tenant.config.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Provides JWT generation and validation
 */
@Configuration
public class JwtUtil {

    private String secret = "54af87trw84bd94w65qr123487vc814658geq198vs";

    private String expirationTime = "500000";

    private Key key;

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    public Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(key)
                .parseClaimsJws(token)
                .getBody();
    }

    public String getUsernameFromToken(String token) {
        return getAllClaimsFromToken(token).getSubject();
    }

    public String getAudienceFromToken(String token) {
        return getAllClaimsFromToken(token).getAudience();
    }

    public Date getExpirationDateFromToken(String token) {
        return getAllClaimsFromToken(token).getExpiration();
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    public String generateToken(UserInfo user, String tenantId) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", user.getRoles());
        return doGenerateToken(claims, user.getUsername(), tenantId);
    }

    private String doGenerateToken(Map<String, Object> claims, String username, String tenantId) {
        Long expirationTimeLong = Long.parseLong(expirationTime); //in second
        final Date createdDate = new Date();
        final Date expirationDate = new Date(createdDate.getTime() + expirationTimeLong * 1000);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setAudience(tenantId)
                .setIssuedAt(createdDate)
                .setExpiration(expirationDate)
                .signWith(key)
                .compact();
    }

    public Boolean validateToken(String token) {
        return !isTokenExpired(token);
    }

}


