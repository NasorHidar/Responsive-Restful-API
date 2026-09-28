package com.example.restapi.service;

import com.example.restapi.dto.TotalBillResponse;
import com.example.restapi.entity.UserAccount;
import com.example.restapi.exception.UserNotFoundException;
import com.example.restapi.repository.UserAccountRepository;
import org.springframework.stereotype.Service;

/**
 * Encapsulates bill-related calculations. Kept thin on purpose —
 * the calculation itself is trivial, but isolating it makes future
 * business rules (discounts, taxes, currency, etc.) easy to add.
 */
@Service
public class UserBillService {

    private final UserAccountRepository userRepository;

    public UserBillService(UserAccountRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Compute {@code totalBill = baseBill + taxOrServiceCharge} for {@code userId}.
     *
     * @throws UserNotFoundException if no user with that id exists.
     */
    public TotalBillResponse computeTotalBill(String userId) {
        UserAccount user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        double total = user.getBaseBill() + user.getTaxOrServiceCharge();
        return new TotalBillResponse(
                user.getUserId(),
                user.getBaseBill(),
                user.getTaxOrServiceCharge(),
                total
        );
    }
}
