package ksw.h_r_recruitment_management_system.service;

import jakarta.transaction.Transactional;
import ksw.h_r_recruitment_management_system.domain.Interview;
import ksw.h_r_recruitment_management_system.domain.InterviewStatus;
import ksw.h_r_recruitment_management_system.repository.InterviewRepository;
import ksw.h_r_recruitment_management_system.repository.OnboardingDocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class InterviewService {

    @Autowired
    private InterviewRepository interviewRepo;
    @Autowired
    private OnboardingDocumentRepository onboardingRepo;

    public static final String HAS_DOCUMENTS = "That interview has onboarding documents, delete them first";

    public static final String NOT_FOUND = "The interview with that id does not exist";

    public static final String PAST_DATE = "Interview date cannot be in the past";
    public static final String RECRUITER_BUSY = "Recruiter already has an interview on that date";
    public static final String MISSING_DATA = "Interview date and recruiter are required";
    public static final String STATUS_UPDATED = "Interview status updated successfully";

    public String saveInterview(Interview i) {
        if(i.getInterviewDate() == null || i.getRecruiter() == null || i.getRecruiter().getId() == null) {
            return MISSING_DATA;
        }
        if (i.getInterviewDate().isBefore(LocalDate.now())) {
            return PAST_DATE;
        }
        if(interviewRepo.existsByRecruiterIdAndInterviewDateAndStatusNot(
                i.getRecruiter().getId(), i.getInterviewDate(), InterviewStatus.REJECTED)){
            return RECRUITER_BUSY;
        }
        i.setStatus(InterviewStatus.INVITED);
        interviewRepo.save(i);
        return "Interview saved successfully";
    }

    public List<Interview> getAllInterviews() {
        return interviewRepo.findAll();
    }

    public Optional<Interview> getInterviewById(UUID id) {
        return interviewRepo.findById(id);
    }
    public String updateInterview(UUID id, Interview updatedInt) {
        Optional<Interview> existing = interviewRepo.findById(id);

        if (existing.isEmpty()) {
            return NOT_FOUND;
        }
        Interview interview = existing.get();
        interview.setInterviewDate(updatedInt.getInterviewDate());
        interview.setVacancyTitle(updatedInt.getVacancyTitle());
        interview.setApplicant(updatedInt.getApplicant());
        interview.setRecruiter(updatedInt.getRecruiter());
        interviewRepo.save(interview);
        return "Interview updated successfully";
    }

    public String deleteInterview(UUID id){
        if(!interviewRepo.existsById(id)){
            return NOT_FOUND;

        }
        if (onboardingRepo.existsByInterviewId(id)) {
            return HAS_DOCUMENTS;
        }
        interviewRepo.deleteById(id);
        return "Interview deleted successfully";
    }

    public List<Interview> getInterviewsByStatus(InterviewStatus status){
        return interviewRepo.findByStatusOrderByInterviewDateAsc(status);
    }

    public List<Interview> getInterviewsBetween(LocalDate start, LocalDate end){
        return interviewRepo.findByInterviewDateBetweenOrderByInterviewDateAsc(start, end);
    }
    public String updateInterviewStatus(UUID id, InterviewStatus newStatus) {
        Optional<Interview> existing = interviewRepo.findById(id);
        if (existing.isEmpty()) {
            return NOT_FOUND;
        }
        Interview interview = existing.get();
        InterviewStatus current = interview.getStatus();

        boolean allowed = false;
        if (current == InterviewStatus.INVITED) {
            allowed = newStatus == InterviewStatus.ATTENDED || newStatus == InterviewStatus.REJECTED;
        } else if (current == InterviewStatus.ATTENDED) {
            allowed = newStatus == InterviewStatus.OFFERED || newStatus == InterviewStatus.REJECTED;
        }
        if (!allowed) {
            return "Cannot change status from " + current + " to " + newStatus;
        }
        interview.setStatus(newStatus);
        interviewRepo.save(interview);
        return STATUS_UPDATED;
    }
    public List<Object[]> getInterviewCountsByStatus() {
        return interviewRepo.countInterviewsByStatus();
    }

    @Transactional
    public String cancelDay(UUID recruiterId, LocalDate date) {
        int cancelled = interviewRepo.cancelInterviewsOfDay(
                recruiterId, date, InterviewStatus.REJECTED, InterviewStatus.OFFERED);
        return cancelled + " interviews cancelled";
    }

}
