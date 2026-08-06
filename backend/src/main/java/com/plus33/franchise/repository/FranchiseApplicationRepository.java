package com.plus33.franchise.repository;

import com.plus33.franchise.model.FranchiseApplication;
import com.plus33.franchise.model.FranchiseApplication.ApplicationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA Repository for FranchiseApplication entity.
 * Provides CRUD + custom queries for the admin dashboard.
 */
@Repository
public interface FranchiseApplicationRepository extends JpaRepository<FranchiseApplication, Long> {

    // Find by email (check duplicate applications)
    Optional<FranchiseApplication> findByEmail(String email);

    // Filter by status (for admin management)
    Page<FranchiseApplication> findByStatus(ApplicationStatus status, Pageable pageable);

    // Filter by country of applicant
    Page<FranchiseApplication> findByCountryIgnoreCase(String country, Pageable pageable);

    // Filter by franchise model preference
    Page<FranchiseApplication> findByFranchiseModelIgnoreCase(String franchiseModel, Pageable pageable);

    // Count by status (dashboard KPIs)
    long countByStatus(ApplicationStatus status);

    // Check if applicant already applied
    boolean existsByEmail(String email);

    // Full-text search by name or email
    @Query("SELECT a FROM FranchiseApplication a WHERE " +
           "LOWER(a.fullName) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(a.email) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(a.city) LIKE LOWER(CONCAT('%', :query, '%'))")
    Page<FranchiseApplication> searchApplications(@Param("query") String query, Pageable pageable);

    // Applications submitted in date range (reporting)
    List<FranchiseApplication> findBySubmittedAtBetween(LocalDateTime start, LocalDateTime end);

    // Recent applications ordered by date
    List<FranchiseApplication> findTop10ByOrderBySubmittedAtDesc();

    // Count all applications by franchise model (analytics)
    @Query("SELECT a.franchiseModel, COUNT(a) FROM FranchiseApplication a GROUP BY a.franchiseModel")
    List<Object[]> countByFranchiseModel();

    // Count applications by country (regional analytics)
    @Query("SELECT a.country, COUNT(a) FROM FranchiseApplication a GROUP BY a.country ORDER BY COUNT(a) DESC")
    List<Object[]> countByCountry();
}
