package KTPM.Backend.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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
    private BigDecimal amount;
    private BigDecimal unitPrice;
    private BigDecimal totalPrice; // amount * unitPrice
    private PaymentDetail.PaymentDetailStatus status;
    private LocalDateTime createdAt;
    private String periodInfo; // Tháng/Năm của kỳ thu
    private String paymentStatus; // Trạng thái của payment nếu có
    private String transactionCode; // Mã giao dịch nếu có
    private String note; // Ghi chú từ kế toán nếu có
} 