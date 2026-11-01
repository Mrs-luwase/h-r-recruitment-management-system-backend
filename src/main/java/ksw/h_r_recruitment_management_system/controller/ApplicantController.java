package ksw.h_r_recruitment_management_system.controller;


import ksw.h_r_recruitment_management_system.domain.Applicant;
import ksw.h_r_recruitment_management_system.domain.InterviewRoom;
import ksw.h_r_recruitment_management_system.service.ApplicantService;
import ksw.h_r_recruitment_management_system.service.InterviewRoomService;
import ksw.h_r_recruitment_management_system.service.RecruiterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/applicants")       
public class ApplicantController {

    @Autowired
    private ApplicantService applicantServ;

    @Autowired
    private RecruiterService recruiterServ;

    @PostMapping("/save")
    public ResponseEntity<String> saveApplicant(@RequestBody Applicant a) {
        String message = applicantServ.saveApplicant(a);

        return new ResponseEntity<>(message, HttpStatus.CREATED);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Applicant>> getAllApplicants() {
        return new ResponseEntity<>(applicantServ.getAllApplicants(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getApplicantById(@PathVariable UUID id) {
        Optional<Applicant> a = applicantServ.getApplicantById(id);
        if (a.isPresent()) {
            return new ResponseEntity<>(a.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(ApplicantService.NOT_FOUND, HttpStatus.NOT_FOUND);
    }
    @GetMapping("/by-last-name")
    public ResponseEntity<List<Applicant>> getApplicantsByLastName(@RequestParam String lastName) {
        return new ResponseEntity<>(applicantServ.getApplicantByLastName(lastName), HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    ResponseEntity<String> updateApplicant(@PathVariable UUID id,
                                           @RequestBody Applicant a) {

        String message = applicantServ.updateApplicant(id, a);

        if (message.equals(ApplicantService.NOT_FOUND)) {
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteApplicant(@PathVariable UUID id) {

        String message = applicantServ.deleteApplicant(id);
        if (message.equals(ApplicantService.NOT_FOUND)) {
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @GetMapping("/of-recruiter/{recruiterId}")
    public ResponseEntity<?> getApplicantsOfRecruiter(@PathVariable UUID recruiterId) {
        if(recruiterServ.getRecruiterById(recruiterId).isEmpty()) {
            return new ResponseEntity<>(RecruiterService.NOT_FOUND, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(applicantServ.getApplicantsOfRecruiter(recruiterId), HttpStatus.OK);

    }
}
