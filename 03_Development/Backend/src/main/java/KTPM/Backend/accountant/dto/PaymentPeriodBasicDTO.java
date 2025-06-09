package KTPM.Backend.accountant.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PaymentPeriodBasicDTO {
    private Integer paymentPeriodId;
    private int month;
    private int year;
}
