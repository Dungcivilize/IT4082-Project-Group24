package KTPM.Backend.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResidentTemporaryAbsentResponse {
    private Integer temporaryAbsentId;
    private Integer residentId;
    private String residentName;
    private String apartmentCode;
    private LocalDate startDate;
    private LocalDate endDate;
    private String temporaryAddress;
    private String reason;
} 