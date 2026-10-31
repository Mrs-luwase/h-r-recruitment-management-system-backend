package ksw.h_r_recruitment_management_system.controller;



import ksw.h_r_recruitment_management_system.domain.Interview;
import ksw.h_r_recruitment_management_system.domain.InterviewStatus;
import ksw.h_r_recruitment_management_system.service.InterviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/interviews")
public class InterviewController {

    @Autowired
    private InterviewService interviewServ;

    @PostMapping("/save")
    public ResponseEntity<String> saveInterview(@RequestBody Interview i) {
        String message = interviewServ.saveInterview(i);
        if (message.equals(InterviewService.PAST_DATE) || message.equals(InterviewService.MISSING_DATA)) {
            return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
        }
        if (message.equals(InterviewService.RECRUITER_BUSY)) {
            return new ResponseEntity<>(message, HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(message, HttpStatus.CREATED);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Interview>> getAllInterviews() {
        return new ResponseEntity<>(interviewServ.getAllInterviews(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getInterviewById(@PathVariable UUID id) {
        Optional<Interview> i = interviewServ.getInterviewById(id);

        if (i.isPresent()) {
            return new ResponseEntity<>(i.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(InterviewService.NOT_FOUND, HttpStatus.NOT_FOUND);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateInterview(@PathVariable UUID id, @RequestBody Interview i) {

        String message = interviewServ.updateInterview(id, i);

        if (message.equals(InterviewService.NOT_FOUND)) {
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(message, HttpStatus.OK);
    }
   @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteInterview(@PathVariable UUID id) {
        String message = interviewServ.deleteInterview(id);

        if(message.equals(InterviewService.NOT_FOUND)) {
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }
        if (message.equals(InterviewService.HAS_DOCUMENTS)) {
            return new ResponseEntity<>(message, HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(message, HttpStatus.OK);
   }

   @GetMapping("/by-status")
    public ResponseEntity<List<Interview>> getInterviewByStatus(@RequestParam InterviewStatus status) {
        return new ResponseEntity<>(interviewServ.getInterviewsByStatus(status), HttpStatus.OK);
   }

   @GetMapping("/between")
    public ResponseEntity<?> getInterviewsBetween(@RequestParam String start, @RequestParam String end) {

        LocalDate startDate;
        LocalDate endDate;
        try {
            startDate = LocalDate.parse(start);
            endDate = LocalDate.parse(end);
        } catch (DateTimeParseException e) {
            return new ResponseEntity<>("Dates must be in the format yyyy-MM-dd", HttpStatus.BAD_REQUEST);

        }
        return new ResponseEntity<>(interviewServ.getInterviewsBetween(startDate, endDate), HttpStatus.OK);
   }

   @PatchMapping("/{id}/status")
    public ResponseEntity<String> updateInterviewStatus(@PathVariable UUID id,
                                                       @RequestParam InterviewStatus status) {
        String message = interviewServ.updateInterviewStatus(id, status);
        if (message.equals(InterviewService.NOT_FOUND)) {
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }
        if(message.equals(InterviewService.STATUS_UPDATED)) {
            return new ResponseEntity<>(message, HttpStatus.OK);

        }
        return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/stats/by-status")
    public ResponseEntity<List<Object[]>> getInterviewCountsByStatus() {
        return new ResponseEntity<>(interviewServ.getInterviewCountsByStatus(), HttpStatus.OK);
    }

    @PatchMapping("/cancel-day")
    public ResponseEntity<String> cancelDay(@RequestParam UUID recruiterId,
                                            @RequestParam String date) {
        LocalDate day;
        try {
            day = LocalDate.parse(date);
        } catch (DateTimeParseException e) {
            return new ResponseEntity<>("Date must be in the format yyyy-MM-dd", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(interviewServ.cancelDay(recruiterId, day), HttpStatus.OK);
    }

}
