package com.KTPM.KTPM.DTO;


public class ServiceRegistrationResponse {
    private Long srId;
    private Long serviceId;
    private String serviceName;
    private String description;
    private Double fee;
    private Integer quantity;

    public ServiceRegistrationResponse(Long srId, Long serviceId, String serviceName, String description, Double fee, Integer quantity) {
        this.srId = srId;
        this.serviceId = serviceId;
        this.serviceName = serviceName;
        this.description = description;
        this.fee = fee;
        this.quantity = quantity;
    }

    public Long getSrId() {
        return srId;
    }

    public void setSrId(Long srId) {
        this.srId = srId;
    }

    public Long getServiceId() {
        return serviceId;
    }

    public void setServiceId(Long serviceId) {
        this.serviceId = serviceId;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getFee() {
        return fee;
    }

    public void setFee(Double fee) {
        this.fee = fee;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
