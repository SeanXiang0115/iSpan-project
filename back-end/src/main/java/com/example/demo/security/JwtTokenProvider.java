package com.example.demo.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.access-token-expiration-ms}")
    private long accessTokenExpirationMs;

    @Value("${jwt.refresh-token-expiration-ms}")
    private long refreshTokenExpirationMs;

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }

    public String generateAccessToken(Authentication authentication) {
        UserDetails userPrincipal = (UserDetails) authentication.getPrincipal();
        // 假設只有一個角色，取出並去除 "ROLE_" 前綴 (如果有的話)，或者直接存完整的權限
        String role = userPrincipal.getAuthorities().stream()
                .findFirst()
                .map(grantedAuthority -> {
                    String authority = grantedAuthority.getAuthority();
                    return authority.startsWith("ROLE_") ? authority.substring(5) : authority;
                })
                .orElse("USER");
        return generateToken(userPrincipal.getUsername(), role, null, accessTokenExpirationMs);
    }

    public String generateAccessToken(String email, String role) {
        return generateToken(email, role, null, accessTokenExpirationMs);
    }

    public String generateAccessToken(String email, String role, String position) {
        return generateToken(email, role, position, accessTokenExpirationMs);
    }

    public String generateRefreshToken(String email) {
        // Refresh token 通常不需要包含 role，但如果需要也可以加
        return generateToken(email, null, null, refreshTokenExpirationMs);
    }

    public String generatePreAuthToken(String email) {
        // 提供一個 5 分鐘的短暫 token 用於 2FA
        return generateToken(email, "PRE_AUTH", null, 5 * 60 * 1000); // 5 minutes
    }

    private String generateToken(String email, String role, String position, long expirationMs) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expirationMs);

        var builder = Jwts.builder()
                .subject(email)
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(getSigningKey());

        if (role != null) {
            builder.claim("role", role);
        }

        if (position != null) {
            builder.claim("position", position);
        }

        return builder.compact();
    }

    public String getEmailFromToken(String token) {
        Claims claims = getClaims(token);
        return claims.getSubject();
    }

    public String getRoleFromToken(String token) {
        Claims claims = getClaims(token);
        return claims.get("role", String.class);
    }

    public String getPositionFromToken(String token) {
        Claims claims = getClaims(token);
        return claims.get("position", String.class);
    }

    private Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }
}
