package KTPM.Backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentPeriodDTO {
    private Integer paymentPeriodId;
    private Integer month;
    private Integer year;
    private boolean completed;
} 