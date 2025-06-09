package KTPM.Backend.accountant.dto;

import lombok.Data;

@Data
public class CreatePaymentPeriodRequestDTO {
    private Integer month;
    private Integer year;
    private String note;
}
