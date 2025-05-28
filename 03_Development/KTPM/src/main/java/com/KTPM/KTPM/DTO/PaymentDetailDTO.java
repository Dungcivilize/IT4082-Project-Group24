package com.KTPM.KTPM.DTO;

import java.util.Date;

public class PaymentDetailDTO {
    private Long citizenId;       // ID của hộ dân
    private String status;        // Trạng thái: PAID hoặc UNPAID
    private Double cost;          // Số tiền
    private Date paymentDate;     // Ngày thanh toán (có thể null nếu chưa nộp)

    public PaymentDetailDTO(Long citizenId, String status, Double cost, Date paymentDate) {
        this.citizenId = citizenId;
        this.status = status;
        this.cost = cost;
        this.paymentDate = paymentDate;
    }

    // Getters và setters

    public Long getCitizenId() {
        return citizenId;
    }

    public void setCitizenId(Long citizenId) {
        this.citizenId = citizenId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Double getCost() {
        return cost;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }

    public Date getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }
}
