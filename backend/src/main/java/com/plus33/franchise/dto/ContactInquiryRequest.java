package com.plus33.franchise.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

/**
 * DTO for incoming contact inquiry from index.html.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContactInquiryRequest {

    @NotBlank(message = "Full name is required")
    private String fullName;

    @NotBlank(message = "Email is required")
    @Email(message = "Must be a valid email")
    private String email;

    private String phone;
    private String country;
    private String franchiseModelInterest;
    private String message;
}
