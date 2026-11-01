package ksw.h_r_recruitment_management_system.controller;

import ksw.h_r_recruitment_management_system.domain.Interview;
import ksw.h_r_recruitment_management_system.domain.InterviewRoom;
import ksw.h_r_recruitment_management_system.service.InterviewRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/interview-rooms")
public class InterviewRoomController {

    @Autowired
    private InterviewRoomService interviewRoomService;

    @PostMapping("/save")                        
    public ResponseEntity<String> saveInterviewRoom(@RequestBody InterviewRoom interviewRoom) {
        String message = interviewRoomService.saveInterviewRoom(interviewRoom);
        return new ResponseEntity<>(message, HttpStatus.CREATED);
    }

    @GetMapping("/all")                          
    public ResponseEntity<List<InterviewRoom>> getAllInterviewRooms() {
        return new ResponseEntity<>(interviewRoomService.getAllInterviewRooms(), HttpStatus.OK);
    }

    @GetMapping("/{id}")                         
    public ResponseEntity<?> getInterviewRoomById(@PathVariable UUID id) {
        Optional<InterviewRoom> room = interviewRoomService.getInterviewRoomById(id);
        if (room.isPresent()) {
            return new ResponseEntity<>(room.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(InterviewRoomService.NOT_FOUND, HttpStatus.NOT_FOUND);
    }

    @PutMapping("/update/{id}")                  
    public ResponseEntity<String> updateInterviewRoom(@PathVariable UUID id,
                                                      @RequestBody InterviewRoom interviewRoom) {
        String message = interviewRoomService.updateInterviewRoom(id, interviewRoom);

        if (message.equals(InterviewRoomService.NOT_FOUND)) {
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")               >
    public ResponseEntity<String> deleteInterviewRoom(@PathVariable UUID id) {
        String message = interviewRoomService.deleteInterviewRoom(id);
        if (message.equals(InterviewRoomService.NOT_FOUND)) {
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @GetMapping("/free")
    public ResponseEntity<List<InterviewRoom>> getFreeInterviewRooms() {
        return new ResponseEntity<>(interviewRoomService.getFreeInterviewRooms(), HttpStatus.OK);
    }
}
