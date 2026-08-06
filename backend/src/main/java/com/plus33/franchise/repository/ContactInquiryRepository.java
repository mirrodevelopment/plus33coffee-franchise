package com.plus33.franchise.repository;

import com.plus33.franchise.model.ContactInquiry;
import com.plus33.franchise.model.ContactInquiry.InquiryStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data JPA Repository for ContactInquiry entity.
 */
@Repository
public interface ContactInquiryRepository extends JpaRepository<ContactInquiry, Long> {

    Page<ContactInquiry> findByStatus(InquiryStatus status, Pageable pageable);

    List<ContactInquiry> findTop20ByOrderByCreatedAtDesc();

    long countByStatus(InquiryStatus status);

    boolean existsByEmail(String email);
}
