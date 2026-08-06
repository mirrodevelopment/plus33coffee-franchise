package com.plus33.franchise.service;

import com.plus33.franchise.dto.FranchiseApplicationRequest;
import com.plus33.franchise.dto.FranchiseApplicationResponse;
import com.plus33.franchise.model.FranchiseApplication;
import com.plus33.franchise.model.FranchiseApplication.ApplicationStatus;
import com.plus33.franchise.repository.FranchiseApplicationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * =========================================================================
 * FranchiseApplicationService — Business Logic Layer
 * =========================================================================
 *
 * Handles all business logic for franchise applications:
 *  - Submission from apply.html
 *  - Admin CRUD operations
 *  - Status management
 *  - Analytics / KPIs
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class FranchiseApplicationService {

    private final FranchiseApplicationRepository applicationRepository;
    private final EmailNotificationService emailNotificationService;

    /**
     * Submit a new franchise application from apply.html.
     * Sends confirmation email to applicant + notification to admin.
     */
    public FranchiseApplicationResponse submitApplication(FranchiseApplicationRequest request) {
        log.info("New franchise application received from: {} <{}>", request.getFullName(), request.getEmail());

        // Check for duplicate application
        if (applicationRepository.existsByEmail(request.getEmail())) {
            log.warn("Duplicate application attempt from email: {}", request.getEmail());
            throw new IllegalStateException(
                "An application with this email already exists. " +
                "Please contact plus33coffee.franchises@gmail.com for support."
            );
        }

        // Map DTO → Entity
        FranchiseApplication application = FranchiseApplication.builder()
                .fullName(request.getFullName())
                .dob(request.getDob())
                .email(request.getEmail())
                .phoneCode(request.getPhoneCode())
                .phone(request.getPhone())
                .address(request.getAddress())
                .city(request.getCity())
                .state(request.getState())
                .country(request.getCountry())
                .zip(request.getZip())
                .occupation(request.getOccupation())
                .employer(request.getEmployer())
                .industry(request.getIndustry())
                .yearsExperience(request.getYearsExperience())
                .experienceDesc(request.getExperienceDesc())
                .franchiseModel(request.getFranchiseModel())
                .locationType(request.getLocationType())
                .region(request.getRegion())
                .hasLocation(request.getHasLocation())
                .locationDetails(request.getLocationDetails())
                .motivation(request.getMotivation())
                .investmentBudget(request.getInvestmentBudget())
                .financeSource(request.getFinanceSource())
                .status(ApplicationStatus.PENDING)
                .build();

        FranchiseApplication saved = applicationRepository.save(application);
        log.info("Application saved with ID: {}", saved.getId());

        // Send async emails (non-blocking)
        emailNotificationService.sendApplicationConfirmationToApplicant(saved);
        emailNotificationService.sendApplicationNotificationToAdmin(saved);

        return toResponse(saved);
    }

    /**
     * Get all applications with pagination and sorting (admin use).
     */
    @Transactional(readOnly = true)
    public Page<FranchiseApplicationResponse> getAllApplications(Pageable pageable) {
        return applicationRepository.findAll(pageable).map(this::toResponse);
    }

    /**
     * Get a single application by ID.
     */
    @Transactional(readOnly = true)
    public FranchiseApplicationResponse getApplicationById(Long id) {
        FranchiseApplication app = applicationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Application not found with ID: " + id));
        return toResponse(app);
    }

    /**
     * Filter applications by status (admin management).
     */
    @Transactional(readOnly = true)
    public Page<FranchiseApplicationResponse> getApplicationsByStatus(ApplicationStatus status, Pageable pageable) {
        return applicationRepository.findByStatus(status, pageable).map(this::toResponse);
    }

    /**
     * Search applications by name/email/city (admin search).
     */
    @Transactional(readOnly = true)
    public Page<FranchiseApplicationResponse> searchApplications(String query, Pageable pageable) {
        return applicationRepository.searchApplications(query, pageable).map(this::toResponse);
    }

    /**
     * Update application status (admin action).
     */
    public FranchiseApplicationResponse updateStatus(Long id, ApplicationStatus newStatus, String adminNotes) {
        FranchiseApplication app = applicationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Application not found with ID: " + id));

        ApplicationStatus previousStatus = app.getStatus();
        app.setStatus(newStatus);
        if (adminNotes != null && !adminNotes.isBlank()) {
            app.setAdminNotes(adminNotes);
        }

        FranchiseApplication updated = applicationRepository.save(app);
        log.info("Application {} status changed: {} → {}", id, previousStatus, newStatus);

        // Notify applicant on significant status changes
        if (newStatus == ApplicationStatus.APPROVED || newStatus == ApplicationStatus.REJECTED) {
            emailNotificationService.sendStatusUpdateToApplicant(updated);
        }

        return toResponse(updated);
    }

    /**
     * Delete an application (admin only — use with caution).
     */
    public void deleteApplication(Long id) {
        if (!applicationRepository.existsById(id)) {
            throw new RuntimeException("Application not found with ID: " + id);
        }
        applicationRepository.deleteById(id);
        log.info("Application {} deleted by admin", id);
    }

    /**
     * Dashboard KPIs — status counts.
     */
    @Transactional(readOnly = true)
    public Map<String, Long> getDashboardStats() {
        return Map.of(
            "total",       applicationRepository.count(),
            "pending",     applicationRepository.countByStatus(ApplicationStatus.PENDING),
            "underReview", applicationRepository.countByStatus(ApplicationStatus.UNDER_REVIEW),
            "shortlisted", applicationRepository.countByStatus(ApplicationStatus.SHORTLISTED),
            "approved",    applicationRepository.countByStatus(ApplicationStatus.APPROVED),
            "rejected",    applicationRepository.countByStatus(ApplicationStatus.REJECTED)
        );
    }

    /**
     * Get recent 10 applications (admin dashboard feed).
     */
    @Transactional(readOnly = true)
    public List<FranchiseApplicationResponse> getRecentApplications() {
        return applicationRepository.findTop10ByOrderBySubmittedAtDesc()
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    /**
     * Analytics — count by franchise model.
     */
    @Transactional(readOnly = true)
    public Map<String, Long> getApplicationsByModel() {
        return applicationRepository.countByFranchiseModel()
                .stream()
                .collect(Collectors.toMap(
                    row -> (String) row[0],
                    row -> (Long) row[1]
                ));
    }

    // -----------------------------------------------------------------------
    // Private helpers
    // -----------------------------------------------------------------------

    private FranchiseApplicationResponse toResponse(FranchiseApplication app) {
        return FranchiseApplicationResponse.builder()
                .id(app.getId())
                .fullName(app.getFullName())
                .email(app.getEmail())
                .phone(app.getPhone())
                .city(app.getCity())
                .state(app.getState())
                .country(app.getCountry())
                .occupation(app.getOccupation())
                .franchiseModel(app.getFranchiseModel())
                .locationType(app.getLocationType())
                .region(app.getRegion())
                .hasLocation(app.getHasLocation())
                .investmentBudget(app.getInvestmentBudget())
                .financeSource(app.getFinanceSource())
                .status(app.getStatus())
                .adminNotes(app.getAdminNotes())
                .submittedAt(app.getSubmittedAt())
                .updatedAt(app.getUpdatedAt())
                .build();
    }
}
