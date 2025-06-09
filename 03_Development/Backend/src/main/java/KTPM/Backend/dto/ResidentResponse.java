package KTPM.Backend.dto;

import java.time.LocalDate;

import KTPM.Backend.entity.Resident;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResidentResponse {
    private Integer residentId;
    private String fullName;
    private LocalDate birthDate;
    private String gender;
    private String identityCard;
    private String phone;
    private String email;
    private String occupation;
    private String residentType;
    private String relationship;
    private String status;
    private String apartmentCode;

    public static ResidentResponse fromEntity(Resident resident) {
        return new ResidentResponse(
            resident.getResidentId(),
            resident.getFullName(),
            resident.getBirthDate(),
            resident.getGender().toString(),
            resident.getIdentityCard(),
            resident.getPhone(),
            resident.getEmail(),
            resident.getOccupation(),
            resident.getResidentType().toString(),
            resident.getRelationship(),
            resident.getStatus().toString(),
            resident.getOwnership().getApartment().getApartmentCode()
        );
    }
} 