package KTPM.Backend.Admin.dto;

import java.time.LocalDate;

import KTPM.Backend.entity.Resident.Gender;
import KTPM.Backend.entity.Resident.ResidentStatus;
import KTPM.Backend.entity.Resident.ResidentType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminResidentDTO {
    private Integer residentId;
    private Integer ownershipId;     
    private String fullName;
    private LocalDate birthDate;
    private Gender gender;
    private String identityCard;
    private String phone;
    private String email;
    private String occupation;
    private ResidentType residentType;
    private String relationship;
    private ResidentStatus status;
}
