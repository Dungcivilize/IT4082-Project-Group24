package KTPM.Backend.dto;

import java.time.LocalDate;

import KTPM.Backend.entity.Resident;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResidentRequest {
    private String fullName;
    private LocalDate birthDate;
    private String gender;
    private String identityCard;
    private String phone;
    private String email;
    private String occupation;
    private String residentType;
    private String relationship;

    public Resident toEntity() {
        Resident resident = new Resident();
        resident.setFullName(this.fullName);
        resident.setBirthDate(this.birthDate);
        resident.setGender(Resident.Gender.valueOf(this.gender.toLowerCase()));
        resident.setIdentityCard(this.identityCard);
        resident.setPhone(this.phone);
        resident.setEmail(this.email);
        resident.setOccupation(this.occupation);
        resident.setResidentType(Resident.ResidentType.valueOf(this.residentType.toLowerCase()));
        resident.setRelationship(this.relationship);
        resident.setStatus(Resident.ResidentStatus.living);
        return resident;
    }
} 