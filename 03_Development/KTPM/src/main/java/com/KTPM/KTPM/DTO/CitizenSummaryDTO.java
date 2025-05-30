package com.KTPM.KTPM.DTO;

public class CitizenSummaryDTO {
    private Long citizenId;
    private String fullname;
    private String phone;

    public CitizenSummaryDTO(Long citizenId, String fullname, String phone) {
        this.citizenId = citizenId;
        this.fullname = fullname;
        this.phone = phone;
    }

    public CitizenSummaryDTO() {
    }

    public Long getCitizenId() {
        return citizenId;
    }

    public void setCitizenId(Long citizenId) {
        this.citizenId = citizenId;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
