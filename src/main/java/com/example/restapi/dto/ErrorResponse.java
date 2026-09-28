package com.example.restapi.dto;

import java.time.Instant;
import java.util.Map;

/**
 * Generic error envelope used by the global exception handler.
 */
public record ErrorResponse(
        int status,
        String error,
        String message,
        Instant timestamp,
        Map<String, String> details
) { }
