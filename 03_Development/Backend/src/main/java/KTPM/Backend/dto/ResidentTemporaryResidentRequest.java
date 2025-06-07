package KTPM.Backend.dto;

import java.time.LocalDate;

import KTPM.Backend.entity.Resident;
import lombok.Data;

@Data
public class ResidentTemporaryResidentRequest {
    private String fullName;
    private LocalDate birthDate;
    private Resident.Gender gender;
    private String identityCard;
    private String phone;
    private LocalDate startDate;
    private LocalDate endDate;
    private String reason;
} 