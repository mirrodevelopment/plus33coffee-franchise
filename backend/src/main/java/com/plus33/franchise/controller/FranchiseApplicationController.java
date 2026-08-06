package com.plus33.franchise.controller;

import com.plus33.franchise.dto.ApiResponse;
import com.plus33.franchise.dto.FranchiseApplicationRequest;
import com.plus33.franchise.dto.FranchiseApplicationResponse;
import com.plus33.franchise.model.FranchiseApplication.ApplicationStatus;
import com.plus33.franchise.service.FranchiseApplicationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * =========================================================================
 * FranchiseApplicationController — REST Controller
 * =========================================================================
 *
 * Base path: /api/applications
 *
 * Public Endpoints:
 *   POST   /api/applications          → Submit franchise application (apply.html)
 *
 * Admin Endpoints (secured):
 *   GET    /api/applications          → List all (paginated)
 *   GET    /api/applications/{id}     → Get single application
 *   GET    /api/applications/search   → Search applications
 *   GET    /api/applications/status/{status} → Filter by status
 *   PATCH  /api/applications/{id}/status    → Update status
 *   DELETE /api/applications/{id}           → Delete application
 *   GET    /api/applications/stats    → Dashboard KPIs
 *   GET    /api/applications/recent   → Latest 10 applications
 *   GET    /api/applications/analytics/model → Count by franchise model
 */
@RestController
@RequestMapping("/api/applications")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:8080", "http://127.0.0.1:5500"})
public class FranchiseApplicationController {

    private final FranchiseApplicationService applicationService;

    // -----------------------------------------------------------------------
    // PUBLIC — Application Submission (called from apply.html)
    // -----------------------------------------------------------------------

    /**
     * POST /api/applications
     * Submit a new franchise application.
     */
    @PostMapping
    public ResponseEntity<ApiResponse<FranchiseApplicationResponse>> submitApplication(
            @Valid @RequestBody FranchiseApplicationRequest request) {

        log.info("POST /api/applications — {} <{}>", request.getFullName(), request.getEmail());

        try {
            FranchiseApplicationResponse response = applicationService.submitApplication(request);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success(
                        "Your franchise application has been received successfully. " +
                        "Our team will contact you within 24-48 hours.",
                        response
                    ));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    // -----------------------------------------------------------------------
    // ADMIN — Application Management
    // -----------------------------------------------------------------------

    /**
     * GET /api/applications?page=0&size=20&sort=submittedAt,desc
     * List all applications (paginated, sorted).
     */
    @GetMapping
    public ResponseEntity<ApiResponse<Page<FranchiseApplicationResponse>>> getAllApplications(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "submittedAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Page<FranchiseApplicationResponse> applications =
                applicationService.getAllApplications(PageRequest.of(page, size, sort));

        return ResponseEntity.ok(ApiResponse.success("Applications retrieved", applications));
    }

    /**
     * GET /api/applications/{id}
     * Get a single application by ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<FranchiseApplicationResponse>> getApplicationById(@PathVariable Long id) {
        FranchiseApplicationResponse app = applicationService.getApplicationById(id);
        return ResponseEntity.ok(ApiResponse.success("Application retrieved", app));
    }

    /**
     * GET /api/applications/search?q=john&page=0&size=10
     * Search applications by name/email/city.
     */
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<FranchiseApplicationResponse>>> searchApplications(
            @RequestParam String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<FranchiseApplicationResponse> results =
                applicationService.searchApplications(q, PageRequest.of(page, size));

        return ResponseEntity.ok(ApiResponse.success("Search results", results));
    }

    /**
     * GET /api/applications/status/{status}
     * Filter applications by status.
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<ApiResponse<Page<FranchiseApplicationResponse>>> getByStatus(
            @PathVariable ApplicationStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        Page<FranchiseApplicationResponse> applications =
                applicationService.getApplicationsByStatus(status, PageRequest.of(page, size));

        return ResponseEntity.ok(ApiResponse.success("Applications by status", applications));
    }

    /**
     * PATCH /api/applications/{id}/status
     * Update application status (admin workflow).
     *
     * Body: { "status": "APPROVED", "adminNotes": "..." }
     */
    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse<FranchiseApplicationResponse>> updateStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {

        ApplicationStatus newStatus = ApplicationStatus.valueOf(body.get("status"));
        String adminNotes = body.get("adminNotes");

        FranchiseApplicationResponse updated = applicationService.updateStatus(id, newStatus, adminNotes);
        return ResponseEntity.ok(ApiResponse.success("Application status updated to " + newStatus, updated));
    }

    /**
     * DELETE /api/applications/{id}
     * Delete an application (admin only).
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteApplication(@PathVariable Long id) {
        applicationService.deleteApplication(id);
        return ResponseEntity.ok(ApiResponse.success("Application deleted successfully"));
    }

    /**
     * GET /api/applications/stats
     * Dashboard KPI summary.
     */
    @GetMapping("/stats")
    public ResponseEntity<ApiResponse<Map<String, Long>>> getDashboardStats() {
        return ResponseEntity.ok(ApiResponse.success("Dashboard stats", applicationService.getDashboardStats()));
    }

    /**
     * GET /api/applications/recent
     * Latest 10 applications for admin dashboard feed.
     */
    @GetMapping("/recent")
    public ResponseEntity<ApiResponse<List<FranchiseApplicationResponse>>> getRecentApplications() {
        return ResponseEntity.ok(ApiResponse.success("Recent applications", applicationService.getRecentApplications()));
    }

    /**
     * GET /api/applications/analytics/model
     * Count by franchise model (Kiosk / Compact Café / Flagship).
     */
    @GetMapping("/analytics/model")
    public ResponseEntity<ApiResponse<Map<String, Long>>> getByModel() {
        return ResponseEntity.ok(ApiResponse.success("Applications by model", applicationService.getApplicationsByModel()));
    }
}
