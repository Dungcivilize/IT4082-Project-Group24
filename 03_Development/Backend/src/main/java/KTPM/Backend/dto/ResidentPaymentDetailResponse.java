package KTPM.Backend.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import KTPM.Backend.entity.Payment;
import KTPM.Backend.entity.PaymentDetail;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResidentPaymentDetailResponse {
    private Integer paymentDetailId;
    private String serviceName;
    private String serviceType;
    private String periodInfo;
    private BigDecimal amount;
    private BigDecimal unitPrice;
    private BigDecimal totalPrice;
    private PaymentDetail.Status status;
    private Payment.PaymentStatus paymentStatus;
    private String transactionCode;
    private LocalDateTime createdAt;
    private LocalDateTime paidAt;

    public ResidentPaymentDetailResponse(
            Integer paymentDetailId,
            Integer month,
            Integer year,
            String serviceName,
            BigDecimal amount,
            BigDecimal unitPrice,
            BigDecimal totalPrice,
            PaymentDetail.Status status,
            Payment.PaymentStatus paymentStatus,
            String transactionCode,
            LocalDateTime paidAt) {
        this.paymentDetailId = paymentDetailId;
        this.serviceName = serviceName;
        this.serviceType = serviceName; // Tạm thời dùng serviceName làm serviceType
        this.periodInfo = String.format("Tháng %d/%d", month, year);
        this.amount = amount;
        this.unitPrice = unitPrice;
        this.totalPrice = totalPrice;
        this.status = status;
        this.paymentStatus = paymentStatus;
        this.transactionCode = transactionCode;
        this.createdAt = LocalDateTime.now(); // Tạm thời dùng thời điểm hiện tại
        this.paidAt = paidAt;
    }
} 