package com.KTPM.KTPM.DTO;

import java.time.LocalDateTime;

public class CitizenDetailResponse {
    private Long citizenId;
    private String cccdId;
    private String job;
    private LocalDateTime dob;
    private String phone;
    private String email;
    private String fullname;
    private String username;
    private Integer roleId;
    private String roleName;
    private Long residentId;
    // Constructor
    public CitizenDetailResponse(Long citizenId, String cccdId, String job, LocalDateTime dob,
                                 String phone, String email, String fullname, String username,
                                 Integer roleId, String roleName,Long residentId) {
        this.citizenId = citizenId;
        this.cccdId = cccdId;
        this.job = job;
        this.dob = dob;
        this.phone = phone;
        this.email = email;
        this.fullname = fullname;
        this.username = username;
        this.roleId = roleId;
        this.roleName = roleName;
        this.residentId = residentId;
    }

    // Getters & Setters (có thể dùng Lombok @Data nếu thích)
    // ...

    public Long getCitizenId() {
        return citizenId;
    }

    public void setCitizenId(Long citizenId) {
        this.citizenId = citizenId;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public Long getResidentId() {
        return residentId;
    }

    public void setResidentId(Long residentId) {
        this.residentId = residentId;
    }
}
