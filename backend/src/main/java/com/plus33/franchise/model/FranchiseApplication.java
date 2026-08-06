package com.plus33.franchise.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * =========================================================================
 * FranchiseApplication — JPA Entity
 * =========================================================================
 *
 * Represents a franchise application submitted through apply.html.
 * All fields map directly to form fields in the frontend:
 *
 *   SECTION 1 — Personal Information
 *     full_name, dob, email, phone_code, phone
 *     address, city, state, country, zip
 *
 *   SECTION 2 — Professional Background
 *     occupation, employer, industry, years_experience, experience_desc
 *
 *   SECTION 3 — Franchise Interest
 *     franchise_model, location_type, region, has_location, location_details
 *     motivation
 *
 *   SECTION 4 — Investment Overview
 *     investment_budget, finance_source
 *
 * Database Table: franchise_applications
 */
@Entity
@Table(name = "franchise_applications")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FranchiseApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // -----------------------------------------------------------------------
    // SECTION 1 — Personal Information
    // -----------------------------------------------------------------------

    @NotBlank(message = "Full name is required")
    @Size(max = 150, message = "Name cannot exceed 150 characters")
    @Column(name = "full_name", nullable = false, length = 150)
    private String fullName;

    @Column(name = "dob")
    private LocalDate dob;

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be a valid email address")
    @Column(name = "email", nullable = false, length = 255)
    private String email;

    @Size(max = 10)
    @Column(name = "phone_code", length = 10)
    private String phoneCode;

    @Size(max = 20)
    @Column(name = "phone", length = 20)
    private String phone;

    @Size(max = 255)
    @Column(name = "address")
    private String address;

    @NotBlank(message = "City is required")
    @Size(max = 100)
    @Column(name = "city", nullable = false, length = 100)
    private String city;

    @NotBlank(message = "State is required")
    @Size(max = 100)
    @Column(name = "state", nullable = false, length = 100)
    private String state;

    @NotBlank(message = "Country is required")
    @Size(max = 100)
    @Column(name = "country", nullable = false, length = 100)
    private String country;

    @Size(max = 20)
    @Column(name = "zip", length = 20)
    private String zip;

    // -----------------------------------------------------------------------
    // SECTION 2 — Professional Background
    // -----------------------------------------------------------------------

    @NotBlank(message = "Occupation is required")
    @Size(max = 100)
    @Column(name = "occupation", nullable = false, length = 100)
    private String occupation;

    @Size(max = 200)
    @Column(name = "employer", length = 200)
    private String employer;

    @Size(max = 100)
    @Column(name = "industry", length = 100)
    private String industry;

    @Size(max = 50)
    @Column(name = "years_experience", length = 50)
    private String yearsExperience;

    @Column(name = "experience_desc", columnDefinition = "TEXT")
    private String experienceDesc;

    // -----------------------------------------------------------------------
    // SECTION 3 — Franchise Interest & Location
    // -----------------------------------------------------------------------

    @NotBlank(message = "Franchise model is required")
    @Size(max = 100)
    @Column(name = "franchise_model", nullable = false, length = 100)
    private String franchiseModel;          // Kiosk / Compact Café / Flagship Café

    @Size(max = 100)
    @Column(name = "location_type", length = 100)
    private String locationType;            // Mall / High Street / Airport / etc.

    @Size(max = 100)
    @Column(name = "region", length = 100)
    private String region;                  // Target region / city

    @Column(name = "has_location")
    private Boolean hasLocation;            // Already has a physical space?

    @Column(name = "location_details", columnDefinition = "TEXT")
    private String locationDetails;

    @Column(name = "motivation", columnDefinition = "TEXT")
    private String motivation;

    // -----------------------------------------------------------------------
    // SECTION 4 — Investment Overview
    // -----------------------------------------------------------------------

    @Size(max = 100)
    @Column(name = "investment_budget", length = 100)
    private String investmentBudget;        // Budget range selected

    @Size(max = 100)
    @Column(name = "finance_source", length = 100)
    private String financeSource;           // Self-funded / Bank Loan / etc.

    // -----------------------------------------------------------------------
    // STATUS & AUDIT
    // -----------------------------------------------------------------------

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    @Builder.Default
    private ApplicationStatus status = ApplicationStatus.PENDING;

    @Column(name = "admin_notes", columnDefinition = "TEXT")
    private String adminNotes;

    @CreationTimestamp
    @Column(name = "submitted_at", updatable = false)
    private LocalDateTime submittedAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // -----------------------------------------------------------------------
    // Application Status Enum
    // -----------------------------------------------------------------------
    public enum ApplicationStatus {
        PENDING,        // Just submitted, awaiting review
        UNDER_REVIEW,   // Being evaluated by the team
        SHORTLISTED,    // Passed initial screening
        APPROVED,       // Approved for franchise
        REJECTED,       // Not proceeding
        ON_HOLD         // Temporarily paused
    }
}
