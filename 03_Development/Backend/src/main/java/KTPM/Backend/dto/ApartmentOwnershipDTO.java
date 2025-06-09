package KTPM.Backend.dto;

import java.time.LocalDate;

import KTPM.Backend.entity.ApartmentOwnership;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApartmentOwnershipDTO {
    private Integer ownershipId;
    private Integer apartmentId;
    private String apartmentCode;
    private Integer userId;
    private String userFullName;
    private LocalDate startDate;
    private LocalDate endDate;
    private String status;

    public static ApartmentOwnershipDTO fromEntity(ApartmentOwnership entity) {
        ApartmentOwnershipDTO dto = new ApartmentOwnershipDTO();
        dto.setOwnershipId(entity.getOwnershipId());
        dto.setApartmentId(entity.getApartment().getApartmentId());
        dto.setApartmentCode(entity.getApartment().getApartmentCode());
        dto.setUserId(entity.getUser().getUserId());
        dto.setUserFullName(entity.getUser().getFullName());
        dto.setStartDate(entity.getStartDate());
        dto.setEndDate(entity.getEndDate());
        dto.setStatus(entity.getStatus().toString());
        return dto;
    }
} 