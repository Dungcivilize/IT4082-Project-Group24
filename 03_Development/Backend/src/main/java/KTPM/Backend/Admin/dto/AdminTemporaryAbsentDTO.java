package KTPM.Backend.Admin.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminTemporaryAbsentDTO {
    private Integer temporaryAbsentId;
    private String fullName; // Từ resident
    private LocalDate startDate;
    private LocalDate endDate;
    private String temporaryAddress;
    private String reason;
}
