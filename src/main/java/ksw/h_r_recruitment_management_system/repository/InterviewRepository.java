package ksw.h_r_recruitment_management_system.repository;

import ksw.h_r_recruitment_management_system.domain.Interview;
import ksw.h_r_recruitment_management_system.domain.InterviewStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface InterviewRepository extends JpaRepository<Interview, UUID> {

    List<Interview> findByStatusOrderByInterviewDateAsc(InterviewStatus status);

    List<Interview> findByInterviewDateBetweenOrderByInterviewDateAsc(LocalDate start, LocalDate end);

    //Does this recruiter already have an interview on this whose status is not the given one (REJECTED)
    boolean existsByRecruiterIdAndInterviewDateAndStatusNot(UUID recruiterId, LocalDate interviewDate, InterviewStatus status);

    // This asks How many interviews are there for each status? A status with no interviews does not appear.
    @Query("""
        SELECT i.status, COUNT(i)
        FROM Interview i
        GROUP BY i.status
        ORDER BY i.status
        """)
    List<Object[]> countInterviewsByStatus();

    // Reject every interview of this recruiter on this date, except those already OFFERED. Returns how many rows matched.
    @Modifying
    @Query("""
            UPDATE Interview i
            SET i.status = :rejected
            WHERE i.recruiter.id = :recruiterId
              AND i.interviewDate = :date
              AND i.status <> :offered
            """)
    int cancelInterviewsOfDay(@Param("recruiterId") UUID recruiterId,
                              @Param("date") LocalDate date,
                              @Param("rejected") InterviewStatus rejected,
                              @Param("offered") InterviewStatus offered);
}
