package com.example.restapi.dto;

/**
 * Response body for {@code GET /api/user/total-bill}.
 * Total is computed as {@code baseBill + taxOrServiceCharge}.
 */
public record TotalBillResponse(
        String userId,
        Double baseBill,
        Double taxOrServiceCharge,
        Double totalBill
) { }
