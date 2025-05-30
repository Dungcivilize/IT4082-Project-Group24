package com.KTPM.KTPM.DTO;

import lombok.Data;
import java.time.LocalDate;

@Data
public class AddPaymentRequest {
    private String cpName;
    private LocalDate collectionDate; // nếu null sẽ dùng LocalDate.now()
    private Long citizenId;           // cũng là resident_id
    private Long accountantId;        // user đang login (kế toán)
    private Double cost;
    private String status;
}
