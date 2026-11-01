package ksw.h_r_recruitment_management_system.repository;

import ksw.h_r_recruitment_management_system.domain.Applicant;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ApplicantRepository extends JpaRepository<Applicant, UUID> {

    List<Applicant> findByLastNameIgnoreCaseOrderByFirstNameAsc(String lastName);

    
    @Query("""
            SELECT DISTINCT a FROM Interview i
            JOIN i.applicant a
            WHERE i.recruiter.id = :recruiterId
            """)
    List<Applicant> findApplicantsOfRecruiter(@Param("recruiterId") UUID recruiterId);
}
