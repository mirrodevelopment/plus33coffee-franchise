package com.plus33.franchise.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

/**
 * =========================================================================
 * ContactInquiry — JPA Entity
 * =========================================================================
 *
 * Captures general contact/inquiry messages from the index.html website.
 * Sections that generate inquiries:
 *   - "Let's Get Started" section (id="lets-get-started")
 *   - FAQ section (id="faq") — contact prompt
 *   - Hero CTA — initial interest capture
 *
 * Database Table: contact_inquiries
 */
@Entity
@Table(name = "contact_inquiries")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContactInquiry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name is required")
    @Size(max = 150)
    @Column(name = "full_name", nullable = false, length = 150)
    private String fullName;

    @NotBlank(message = "Email is required")
    @Email
    @Column(name = "email", nullable = false, length = 255)
    private String email;

    @Size(max = 20)
    @Column(name = "phone", length = 20)
    private String phone;

    @Size(max = 100)
    @Column(name = "country", length = 100)
    private String country;

    @Size(max = 100)
    @Column(name = "franchise_model_interest", length = 100)
    private String franchiseModelInterest;  // Which store type they're interested in

    @Column(name = "message", columnDefinition = "TEXT")
    private String message;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 30)
    @Builder.Default
    private InquiryStatus status = InquiryStatus.NEW;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    public enum InquiryStatus {
        NEW, CONTACTED, CONVERTED, CLOSED
    }
}
