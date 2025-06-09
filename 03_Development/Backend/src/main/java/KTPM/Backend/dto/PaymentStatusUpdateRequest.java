package KTPM.Backend.dto;

import KTPM.Backend.entity.PaymentDetail.Status;
import lombok.Data;

@Data
public class PaymentStatusUpdateRequest {
    private Integer paymentDetailId;
    private Status status;
    private String note;
} 