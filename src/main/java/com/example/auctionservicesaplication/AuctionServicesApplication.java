package com.example.auctionservicesaplication;

import com.example.auctionservicesaplication.security.SecurityConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(SecurityConfig.class)
public class AuctionServicesApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuctionServicesApplication.class, args);
    }

}
