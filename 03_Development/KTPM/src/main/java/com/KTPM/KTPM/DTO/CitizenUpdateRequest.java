package com.KTPM.KTPM.DTO;

import java.time.LocalDateTime;

public class CitizenUpdateRequest {
    // Citizen fields
    private String cccdId;
    private String job;
    private LocalDateTime dob;

    // User fields
    private String phone;
    private String fullname;
    private String email;

    // Getters and setters

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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

