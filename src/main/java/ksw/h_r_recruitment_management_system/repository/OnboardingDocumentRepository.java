package ksw.h_r_recruitment_management_system.repository;

import ksw.h_r_recruitment_management_system.domain.OnboardingDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface OnboardingDocumentRepository extends JpaRepository<OnboardingDocument, UUID> {

    
    @Query("""
            SELECT d FROM OnboardingDocument d
            WHERE LOWER(d.interview.applicant.email) = LOWER(:email)
            ORDER BY d.receivedDate DESC
            """)
    List<OnboardingDocument> findDocumentsOfApplicant(@Param("email") String email);

    
    boolean existsByInterviewId(UUID interviewId);
}