package KTPM.Backend.dto;

import lombok.Data;
 
@Data
public class ResidentPaymentRequest {
    private Integer paymentDetailId;
    private String transactionCode;
} 