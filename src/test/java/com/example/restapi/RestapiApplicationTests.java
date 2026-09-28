package com.example.restapi;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * Smoke test: confirms the Spring context boots with the default H2 profile.
 */
@SpringBootTest
@ActiveProfiles("h2")
class RestapiApplicationTests {

    @Test
    void contextLoads() {
        // intentionally empty - assertion is "context starts up successfully"
    }
}
