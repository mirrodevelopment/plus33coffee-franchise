package com.plus33.franchise;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * =========================================================================
 * PLUS33 CAFÉ FRANÇAIS — FRANCHISE MANAGEMENT SYSTEM
 * =========================================================================
 *
 * Entry point for the Spring Boot application.
 *
 * Architecture Overview:
 *  - REST API backend for the PLUS33 franchise website
 *  - Handles franchise applications submitted via apply.html
 *  - Sends email notifications using Spring Mail
 *  - Stores applicant data in MySQL (prod) / H2 (dev)
 *  - Serves CORS-enabled API for the static frontend
 *
 * Sections from the frontend served by this backend:
 *  - /api/applications   → Franchise application form (apply.html)
 *  - /api/inquiries      → Contact/inquiry capture from index.html
 *  - /api/admin          → Admin dashboard for managing applications
 *  - /api/health         → Spring Actuator health check
 *
 * @author  PLUS33 Engineering
 * @version 1.0.0
 */
@SpringBootApplication
@EnableAsync
public class FranchiseApplication {

    public static void main(String[] args) {
        SpringApplication.run(FranchiseApplication.class, args);
    }
}
