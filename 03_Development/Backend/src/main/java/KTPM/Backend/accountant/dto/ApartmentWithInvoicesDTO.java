package KTPM.Backend.accountant.dto;

import lombok.Data;
import java.util.List;

@Data
public class ApartmentWithInvoicesDTO {
    private Integer apartmentId;
    private String apartmentCode;
    private List<PaymentDetailDTO> invoices;
}
