package KTPM.Backend.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ResidentPaymentRequest {
    private Integer paymentDetailId;
    private String transactionCode;
    private Integer paymentPeriodId;
} 