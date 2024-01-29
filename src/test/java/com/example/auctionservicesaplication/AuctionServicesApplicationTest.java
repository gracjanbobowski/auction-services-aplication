package com.example.auctionservicesaplication;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ContextConfiguration(classes = AuctionServicesApplication.class)
public class AuctionServicesApplicationTest {

    // Test 1: Context Loads - Ensures the application context loads properly
    @Test
    public void contextLoads() {
        // This test will pass if the application context loads successfully
    }
}
