package com.example.restapi.dto;

/**
 * Response body returned by {@code POST /api/auth/login}.
 * The JWT in {@code token} is what clients send to protected endpoints.
 */
public record LoginResponse(
        String token,
        String tokenType,
        long expiresInMillis,
        String userId
) { }
