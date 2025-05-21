package com.KTPM.KTPM.DTO;

import com.KTPM.KTPM.Models.Payment;

public class PaymentDTO {
    private Long paymentId;
    private Long citizenId;
    private Long cpId;
    private Double cost;
    private String cpName;
    private String collectionDate;

    // Constructor
    public PaymentDTO(Payment payment) {
        this.paymentId = payment.getPaymentId();
        this.citizenId = payment.getCitizen().getCitizenId();
        this.cpId = payment.getCollectionPeriod().getCpId();
        this.cost = payment.getCost();
        this.cpName = payment.getCollectionPeriod().getCpName();
        this.collectionDate = payment.getCollectionPeriod().getCollectionDate().toString();
    }

    // Getters and Setters
    public Long getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(Long paymentId) {
        this.paymentId = paymentId;
    }

    public Long getCitizenId() {
        return citizenId;
    }

    public void setCitizenId(Long citizenId) {
        this.citizenId = citizenId;
    }

    public Long getCpId() {
        return cpId;
    }

    public void setCpId(Long cpId) {
        this.cpId = cpId;
    }

    public Double getCost() {
        return cost;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }

    public String getCpName() {
        return cpName;
    }

    public void setCpName(String cpName) {
        this.cpName = cpName;
    }

    public String getCollectionDate() {
        return collectionDate;
    }

    public void setCollectionDate(String collectionDate) {
        this.collectionDate = collectionDate;
    }
}