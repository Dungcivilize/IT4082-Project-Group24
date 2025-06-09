package KTPM.Backend.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PaymentDetailRequest {
    private Integer serviceTypeId;
    private Integer ownershipId;
    private Integer paymentPeriodId;
    private Double amount;
} 