package KTPM.Backend.accountant.dto;

import lombok.Data;
import java.util.List;

@Data
public class PaymentPeriodWithApartmentsDTO {
    private Integer paymentPeriodId;
    private int month;
    private int year;
    private List<ApartmentWithInvoicesDTO> apartments;
}
