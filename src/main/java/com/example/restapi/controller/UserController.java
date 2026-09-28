package com.example.restapi.controller;

import com.example.restapi.dto.TotalBillResponse;
import com.example.restapi.service.UserBillService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Protected endpoints that require a valid JWT.
 *
 * <p>The {@code @AuthenticationPrincipal} parameter receives the {@code userId}
 * string that {@link com.example.restapi.security.JwtAuthenticationFilter}
 * placed in the SecurityContext.
 */
@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserBillService billService;

    public UserController(UserBillService billService) {
        this.billService = billService;
    }

    /**
     * Calculate the total bill for the authenticated user.
     *
     * <pre>
     * GET /api/user/total-bill
     * Authorization: Bearer &lt;jwt&gt;
     * </pre>
     */
    @GetMapping("/total-bill")
    public ResponseEntity<TotalBillResponse> totalBill(
            @AuthenticationPrincipal String userId
    ) {
        return ResponseEntity.ok(billService.computeTotalBill(userId));
    }
}
