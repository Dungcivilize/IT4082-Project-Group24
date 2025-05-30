package com.KTPM.KTPM.DTO;

public class ResidentDTO {
    private Long residentId;
    private Long householderId;
    private String address;

    public ResidentDTO() {
    }

    public ResidentDTO(Long residentId, Long householderId, String address) {
        this.residentId = residentId;
        this.householderId = householderId;
        this.address = address;
    }

    public Long getResidentId() {
        return residentId;
    }

    public void setResidentId(Long residentId) {
        this.residentId = residentId;
    }

    public Long getHouseholderId() {
        return householderId;
    }

    public void setHouseholderId(Long householderId) {
        this.householderId = householderId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
