package KTPM.Backend.dto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class ResidentTemporaryAbsentRequest {
    private LocalDate startDate;
    private LocalDate endDate;
    private String temporaryAddress;
    private String reason;
} 