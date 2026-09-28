package com.example.restapi.repository;

import com.example.restapi.entity.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data JPA repository for {@link UserAccount}.
 *
 * Standard CRUD plus a finder used by the authentication flow.
 */
@Repository
public interface UserAccountRepository extends JpaRepository<UserAccount, Long> {

    /** Look up a user by their (unique) login id. */
    Optional<UserAccount> findByUserId(String userId);
}
