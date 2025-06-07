package KTPM.Backend.dto;

import java.time.LocalDate;

import KTPM.Backend.entity.Resident;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResidentTemporaryResidentResponse {
    private Integer temporaryResidentId;
    private String fullName;
    private LocalDate birthDate;
    private Resident.Gender gender;
    private String identityCard;
    private String phone;
    private LocalDate startDate;
    private LocalDate endDate;
    private String reason;
    private String apartmentCode;
} 