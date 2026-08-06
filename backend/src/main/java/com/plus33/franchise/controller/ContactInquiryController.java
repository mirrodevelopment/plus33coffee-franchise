package com.plus33.franchise.controller;

import com.plus33.franchise.dto.ApiResponse;
import com.plus33.franchise.dto.ContactInquiryRequest;
import com.plus33.franchise.model.ContactInquiry;
import com.plus33.franchise.service.ContactInquiryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * =========================================================================
 * ContactInquiryController — REST Controller
 * =========================================================================
 *
 * Base path: /api/inquiries
 *
 * POST /api/inquiries          → Submit a contact inquiry from index.html
 * GET  /api/inquiries          → List all inquiries (admin)
 * GET  /api/inquiries/recent   → Recent 20 inquiries
 * PATCH /api/inquiries/{id}/status → Update inquiry status
 */
@RestController
@RequestMapping("/api/inquiries")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:8080", "http://127.0.0.1:5500"})
public class ContactInquiryController {

    private final ContactInquiryService inquiryService;

    /**
     * POST /api/inquiries
     * Submit a new contact inquiry from the PLUS33 website.
     */
    @PostMapping
    public ResponseEntity<ApiResponse<ContactInquiry>> submitInquiry(
            @Valid @RequestBody ContactInquiryRequest request) {

        ContactInquiry inquiry = inquiryService.submitInquiry(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Your inquiry has been received. We'll be in touch soon!", inquiry));
    }

    /**
     * GET /api/inquiries?page=0&size=20
     */
    @GetMapping
    public ResponseEntity<ApiResponse<Page<ContactInquiry>>> getAllInquiries(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        Page<ContactInquiry> inquiries = inquiryService.getAllInquiries(PageRequest.of(page, size));
        return ResponseEntity.ok(ApiResponse.success("Inquiries retrieved", inquiries));
    }

    /**
     * GET /api/inquiries/recent
     */
    @GetMapping("/recent")
    public ResponseEntity<ApiResponse<List<ContactInquiry>>> getRecentInquiries() {
        return ResponseEntity.ok(ApiResponse.success("Recent inquiries", inquiryService.getRecentInquiries()));
    }

    /**
     * PATCH /api/inquiries/{id}/status
     * Body: { "status": "CONTACTED" }
     */
    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse<ContactInquiry>> updateStatus(
            @PathVariable Long id,
            @RequestBody java.util.Map<String, String> body) {

        ContactInquiry.InquiryStatus status = ContactInquiry.InquiryStatus.valueOf(body.get("status"));
        ContactInquiry updated = inquiryService.updateStatus(id, status);
        return ResponseEntity.ok(ApiResponse.success("Inquiry status updated", updated));
    }
}
