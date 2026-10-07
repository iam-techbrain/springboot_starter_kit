package com.example.starterapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Main entry point for the Spring Boot Enterprise REST API Starter Kit.
 * Enables JPA Auditing for automatic entity timestamp tracking.
 */
@SpringBootApplication
@EnableJpaAuditing
public class StarterApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(StarterApiApplication.class, args);
    }
}
