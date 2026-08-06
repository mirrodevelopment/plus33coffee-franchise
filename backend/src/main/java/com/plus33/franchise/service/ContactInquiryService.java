package com.plus33.franchise.service;

import com.plus33.franchise.dto.ContactInquiryRequest;
import com.plus33.franchise.model.ContactInquiry;
import com.plus33.franchise.model.ContactInquiry.InquiryStatus;
import com.plus33.franchise.repository.ContactInquiryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Business logic for contact inquiries from index.html.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ContactInquiryService {

    private final ContactInquiryRepository inquiryRepository;

    /**
     * Submit a contact inquiry from index.html CTA / interest forms.
     */
    public ContactInquiry submitInquiry(ContactInquiryRequest request) {
        log.info("New contact inquiry from: {} <{}>", request.getFullName(), request.getEmail());

        ContactInquiry inquiry = ContactInquiry.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .country(request.getCountry())
                .franchiseModelInterest(request.getFranchiseModelInterest())
                .message(request.getMessage())
                .status(InquiryStatus.NEW)
                .build();

        return inquiryRepository.save(inquiry);
    }

    @Transactional(readOnly = true)
    public List<ContactInquiry> getRecentInquiries() {
        return inquiryRepository.findTop20ByOrderByCreatedAtDesc();
    }

    @Transactional(readOnly = true)
    public Page<ContactInquiry> getAllInquiries(Pageable pageable) {
        return inquiryRepository.findAll(pageable);
    }

    public ContactInquiry updateStatus(Long id, InquiryStatus status) {
        ContactInquiry inquiry = inquiryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Inquiry not found: " + id));
        inquiry.setStatus(status);
        return inquiryRepository.save(inquiry);
    }
}
