package KTPM.Backend.accountant.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountantPaymentPeriodResponse {
    private Integer paymentPeriodId;
    private Integer month;
    private Integer year;
    private String status;
    private String note;

    private List<ApartmentPaymentDetailDto> apartments;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ApartmentPaymentDetailDto {
        private Integer apartmentId;
        private String apartmentCode;
        private List<PaymentDetailDto> paymentDetails;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PaymentDetailDto {
        private Integer paymentDetailId;
        private String serviceName;
        private String serviceType;
        private Double amount;
        private String status;
        private String createdAt;
        private Integer quantity;
        private Double unitPrice;
    }
}
