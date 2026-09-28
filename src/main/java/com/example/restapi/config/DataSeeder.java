package com.example.restapi.config;

import com.example.restapi.entity.UserAccount;
import com.example.restapi.repository.UserAccountRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Seeds the database with two demo users at startup.
 *
 * <p>Uses {@code findByUserId(...).isPresent()} so the seed only fires once —
 * re-running the app against an H2 in-memory DB is fine, and re-running
 * against MySQL/Postgres will not create duplicates.
 */
@Configuration
public class DataSeeder {

    private static final Logger log = LoggerFactory.getLogger(DataSeeder.class);

    @Bean
    CommandLineRunner seedUsers(UserAccountRepository repo) {
        return args -> {
            List<UserAccount> seedData = List.of(
                    UserAccount.builder()
                            .userId("alice")
                            .password("password123")
                            .baseBill(1000.00)
                            .taxOrServiceCharge(150.00)
                            .build(),
                    UserAccount.builder()
                            .userId("bob")
                            .password("secret456")
                            .baseBill(2500.50)
                            .taxOrServiceCharge(375.25)
                            .build()
            );

            for (UserAccount user : seedData) {
                repo.findByUserId(user.getUserId()).ifPresentOrElse(
                        existing -> log.info("Seed: user '{}' already exists, skipping",
                                existing.getUserId()),
                        () -> {
                            repo.save(user);
                            log.info("Seed: inserted user '{}'", user.getUserId());
                        }
                );
            }
        };
    }
}
