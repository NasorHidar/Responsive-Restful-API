package com.example.restapi.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

/**
 * JWT helper that:
 *  - holds the signing key (HMAC-SHA256)
 *  - generates a signed token for an authenticated user
 *  - validates an incoming token and extracts its claims
 *
 * <p>The signing key is provided as a Base64 string in application properties
 * ({@code app.security.jwt.secret}) so it can be rotated via environment
 * variables in production.
 */
@Component
public class JwtTokenProvider {

    private final String secretBase64;
    private final long expirationMs;

    /** Decoded signing key. Recomputed at startup. */
    private SecretKey signingKey;

    public JwtTokenProvider(
            @Value("${app.security.jwt.secret}") String secretBase64,
            @Value("${app.security.jwt.expiration-ms}") long expirationMs
    ) {
        this.secretBase64 = secretBase64;
        this.expirationMs = expirationMs;
    }

    /** Decode the Base64 secret into a {@link SecretKey}. */
    @PostConstruct
    void init() {
        byte[] keyBytes = Decoders.BASE64.decode(secretBase64);
        if (keyBytes.length < 32) {
            throw new IllegalStateException(
                    "JWT secret must be at least 256 bits (32 bytes) after Base64 decoding");
        }
        this.signingKey = Keys.hmacShaKeyFor(keyBytes);
    }

    /** Build a JWT that carries the given {@code userId} as its subject. */
    public String generateToken(String userId) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + expirationMs);

        return Jwts.builder()
                .subject(userId)
                .issuedAt(now)
                .expiration(expiry)
                .signWith(signingKey, Jwts.SIG.HS256)
                .compact();
    }

    /**
     * Parse a token and return its claims.
     *
     * @throws JwtException if the token is invalid, expired, or tampered with.
     */
    public Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(signingKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /** Convenience: extract the {@code sub} (userId) claim. */
    public String getUserIdFromToken(String token) {
        return parseClaims(token).getSubject();
    }

    /** Validate signature & expiration without throwing. */
    public boolean validateToken(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException ex) {
            return false;
        }
    }

    /** Token lifetime in milliseconds (useful for the login response). */
    public long getExpirationMs() {
        return expirationMs;
    }
}
