package com.KTPM.KTPM.Models;


import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "citizen")
public class Citizen {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "citizen_id")
    private Long citizenId;

    @Column(name = "resident_id")
    private Long residentId;

    @Column(name = "cccd_id", nullable = false, unique = true)
    private String cccdId;

    @Column(name = "job", nullable = false)
    private String job;

    @Column(name = "dob", nullable = false)
    private LocalDateTime dob;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Constructors
    public Citizen() {}

    public Citizen(Long residentId, String cccdId, String job, LocalDateTime dob, User user) {
        this.residentId = residentId;
        this.cccdId = cccdId;
        this.job = job;
        this.dob = dob;
        this.user = user;
    }

    // Getters and setters

    public Long getCitizenId() {
        return citizenId;
    }

    public void setCitizenId(Long citizenId) {
        this.citizenId = citizenId;
    }

    public Long getResidentId() {
        return residentId;
    }

    public void setResidentId(Long residentId) {
        this.residentId = residentId;
    }

    public String getCccdId() {
        return cccdId;
    }

    public void setCccdId(String cccdId) {
        this.cccdId = cccdId;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public LocalDateTime getDob() {
        return dob;
    }

    public void setDob(LocalDateTime dob) {
        this.dob = dob;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
