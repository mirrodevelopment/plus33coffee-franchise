package com.plus33.franchise.dto;

import com.plus33.franchise.model.FranchiseApplication.ApplicationStatus;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * FranchiseApplicationResponse — DTO returned to API clients.
 * Sanitized view of the entity, safe for public/admin consumption.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FranchiseApplicationResponse {

    private Long id;
    private String fullName;
    private String email;
    private String phone;
    private String city;
    private String state;
    private String country;
    private String occupation;
    private String franchiseModel;
    private String locationType;
    private String region;
    private Boolean hasLocation;
    private String investmentBudget;
    private String financeSource;
    private ApplicationStatus status;
    private String adminNotes;
    private LocalDateTime submittedAt;
    private LocalDateTime updatedAt;
}
