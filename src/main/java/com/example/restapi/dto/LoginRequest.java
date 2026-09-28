package com.example.restapi.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * Request body for {@code POST /api/auth/login}.
 */
public record LoginRequest(
        @NotBlank(message = "userId must not be blank") String userId,
        @NotBlank(message = "password must not be blank") String password
) { }
