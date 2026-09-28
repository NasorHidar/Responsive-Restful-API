package com.example.restapi.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * JPA entity that maps to the {@code user_account} table.
 *
 * <p>Fields:
 * <ul>
 *   <li>{@code id}                  - auto-generated PK (Long)</li>
 *   <li>{@code userId}              - unique login id (String)</li>
 *   <li>{@code password}            - stored as-is for the demo
 *                                     (use BCrypt in real production code)</li>
 *   <li>{@code baseBill}            - bill amount (Data 1)</li>
 *   <li>{@code taxOrServiceCharge}  - charge amount (Data 2)</li>
 * </ul>
 */
@Entity
@Table(name = "user_account")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserAccount {

    /** Surrogate primary key. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Login id - must be unique across users. */
    @Column(name = "user_id", nullable = false, unique = true, length = 64)
    private String userId;

    /** Stored password. */
    @Column(name = "password", nullable = false)
    private String password;

    /** Bill base amount (Data 1). */
    @Column(name = "base_bill", nullable = false)
    private Double baseBill;

    /** Tax or service charge amount (Data 2). */
    @Column(name = "tax_or_service_charge", nullable = false)
    private Double taxOrServiceCharge;
}
