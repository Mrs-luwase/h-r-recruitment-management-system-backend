package ksw.h_r_recruitment_management_system.domain;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "interview_room")
public class InterviewRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "room_number")
    private int roomNumber;

    @Column(name = "name")
    private String name;

    //class constructor
    public InterviewRoom() {
    }

    //Getters n setters for the fields
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(int roomNumber) {
        this.roomNumber = roomNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
