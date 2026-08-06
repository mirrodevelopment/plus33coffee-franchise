package com.plus33.franchise.dto;

import com.plus33.franchise.model.FranchiseApplication.ApplicationStatus;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDate;

/**
 * =========================================================================
 * FranchiseApplicationRequest — DTO (Data Transfer Object)
 * =========================================================================
 *
 * Incoming request payload from apply.html form submission.
 * Maps to all form fields:
 *   full_name, dob, email, phone_code, phone,
 *   address, city, state, country, zip,
 *   occupation, employer, industry, years_experience, experience_desc,
 *   franchise_model, location_type, region, has_location, location_details,
 *   motivation, investment_budget, finance_source
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FranchiseApplicationRequest {

    // -- Personal Information --
    @NotBlank(message = "Full name is required")
    private String fullName;

    private LocalDate dob;

    @NotBlank(message = "Email is required")
    @Email(message = "Must be a valid email")
    private String email;

    private String phoneCode;
    private String phone;
    private String address;

    @NotBlank(message = "City is required")
    private String city;

    @NotBlank(message = "State is required")
    private String state;

    @NotBlank(message = "Country is required")
    private String country;

    private String zip;

    // -- Professional Background --
    @NotBlank(message = "Occupation is required")
    private String occupation;

    private String employer;
    private String industry;
    private String yearsExperience;
    private String experienceDesc;

    // -- Franchise Preferences --
    @NotBlank(message = "Franchise model is required")
    private String franchiseModel;

    private String locationType;
    private String region;
    private Boolean hasLocation;
    private String locationDetails;
    private String motivation;

    // -- Investment --
    private String investmentBudget;
    private String financeSource;
}
