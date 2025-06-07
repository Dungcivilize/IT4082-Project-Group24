package KTPM.Backend.dto;

import java.time.LocalDate;

import KTPM.Backend.entity.Resident;
import lombok.Data;

@Data
public class ResidentRequest {
    private String fullName;
    private LocalDate birthDate;
    private Resident.Gender gender;
    private String identityCard;
    private String phone;
    private String email;
    private String occupation;
    private Resident.ResidentType residentType;
    private String relationship;
    private Resident.ResidentStatus status;
} 