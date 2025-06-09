package KTPM.Backend.dto;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class ServiceUsageRequest {
    private Integer paymentPeriodId;
    private Integer ownershipId;
    private Integer serviceTypeId;
    private BigDecimal amount;
} 