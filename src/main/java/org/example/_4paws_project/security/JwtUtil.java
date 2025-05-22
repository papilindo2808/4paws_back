package org.example._4paws_project.security;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

import org.example._4paws_project.models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtUtil {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);

    @Value("${jwt.secret}")
    private String secretKey;

    private Key getSignInKey() {
        if (secretKey.length() < 32) {
            throw new IllegalArgumentException("The secret key must be at least 32 characters long.");
        }
        logger.debug("Using secret key (length: {}): {}", secretKey.length(), secretKey); // Log key length and key (remove in production)
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    // Método que solo genera el token sin necesidad de guardarlo en la base de datos
    public String generateToken(User user) {
        String token = Jwts.builder()
                .setSubject(user.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24)) // 24 horas
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();

        logger.debug("Generated JWT token: {}", token); // Log the generated token
        return token;
    }

    // Validar el token
    public boolean validateToken(String token, String username) {
        try {
            boolean isValid = username.equals(extractUsername(token)) && !isTokenExpired(token);
            logger.debug("Token validation for username {}: {} - token expired: {}", username, isValid, isTokenExpired(token));
            return isValid;
        } catch (Exception e) {
            logger.error("Token validation failed: {}, Token: {}", e.getMessage(), token);
            return false;
        }
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public Claims extractAllClaims(String token) throws ExpiredJwtException {
        try {
            logger.debug("Extracting claims from token: {}", token); // Log the token being validated
            return Jwts.parserBuilder()
                    .setSigningKey(getSignInKey())  // Ensure the correct signing key is used
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (io.jsonwebtoken.security.SignatureException e) {
            logger.error("JWT signature validation failed: {}", e.getMessage());
            logger.debug("Token causing the issue: {}", token); // Log the token for debugging
            throw e;
        }
    }
}