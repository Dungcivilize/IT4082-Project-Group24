package KTPM.Backend.accountant.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class PaymentDetailDTO {
    private Integer paymentDetailId;
    private String serviceName;
    private String serviceType;
    private BigDecimal amount;
    private BigDecimal unitPrice;
    private String status;

    public BigDecimal getTotalPrice() {
        return amount.multiply(unitPrice);
    }
}
