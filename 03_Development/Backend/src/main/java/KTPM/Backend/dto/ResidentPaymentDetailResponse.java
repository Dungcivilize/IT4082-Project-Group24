package KTPM.Backend.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import KTPM.Backend.entity.PaymentDetail;
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

    public static ResidentPaymentDetailResponse fromEntity(PaymentDetail entity) {
        return ResidentPaymentDetailResponse.builder()
                .paymentDetailId(entity.getPaymentDetailId())
                .periodInfo(entity.getPaymentPeriod().getMonth() + "/" + entity.getPaymentPeriod().getYear())
                .serviceTypeName(entity.getServiceType().getServiceName())
                .amount(entity.getAmount())
                .unitPrice(entity.getServiceType().getUnitPrice())
                .price(entity.getPrice())
                .status(entity.getStatus())
                .transactionCode(entity.getTransactionCode())
                .createdAt(entity.getCreatedAt())
                .paidAt(entity.getPaidAt())
                .apartmentCode(entity.getOwnership().getApartment().getApartmentCode())
                .ownerName(entity.getOwnership().getUser().getFullName())
                .note(entity.getNote())
                .build();
    }
} 