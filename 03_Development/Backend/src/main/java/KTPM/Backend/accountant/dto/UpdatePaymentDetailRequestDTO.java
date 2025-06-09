package KTPM.Backend.accountant.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class UpdatePaymentDetailRequestDTO {
    private BigDecimal amount;
}
