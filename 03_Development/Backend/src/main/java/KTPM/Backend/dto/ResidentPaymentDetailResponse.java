package KTPM.Backend.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import KTPM.Backend.entity.PaymentDetail.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResidentPaymentDetailResponse {
    private Integer paymentDetailId;
    private String periodInfo;
    private String serviceTypeName;
    private BigDecimal amount;
    private BigDecimal unitPrice;
    private BigDecimal price;
    private Status status;
    private String transactionCode;
    private LocalDateTime createdAt;
    private LocalDateTime paidAt;
    private String apartmentCode;
    private String ownerName;
    private String note;
} 