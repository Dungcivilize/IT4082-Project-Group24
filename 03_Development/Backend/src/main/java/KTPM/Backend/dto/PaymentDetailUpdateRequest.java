package KTPM.Backend.dto;

import java.math.BigDecimal;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PaymentDetailUpdateRequest {
    private Integer paymentDetailId;
    private BigDecimal amount;
    private String note;
} 