package com.plus33.franchise;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * Spring Boot context load test.
 * Verifies the entire application context loads without errors.
 */
@SpringBootTest
@ActiveProfiles("test")
class FranchiseApplicationTests {

    @Test
    void contextLoads() {
        // If this test passes, the Spring context loaded successfully
    }
}
