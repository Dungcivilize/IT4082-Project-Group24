package com.KTPM.KTPM.DTO;

public class ServiceRegistrationRequest {
    private Long residentId;
    private Long serviceId;
    private Integer quantity = 1;

    // Getters và setters...

    public Long getResidentId() {
        return residentId;
    }

    public void setResidentId(Long residentId) {
        this.residentId = residentId;
    }

    public Long getServiceId() {
        return serviceId;
    }

    public void setServiceId(Long serviceId) {
        this.serviceId = serviceId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}

