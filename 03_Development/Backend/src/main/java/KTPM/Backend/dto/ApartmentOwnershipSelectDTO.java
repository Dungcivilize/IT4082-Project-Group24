package KTPM.Backend.dto;

import KTPM.Backend.entity.ApartmentOwnership;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApartmentOwnershipSelectDTO {
    private Integer ownershipId;
    private String label;

    public static ApartmentOwnershipSelectDTO fromEntity(ApartmentOwnership entity) {
        ApartmentOwnershipSelectDTO dto = new ApartmentOwnershipSelectDTO();
        dto.setOwnershipId(entity.getOwnershipId());
        dto.setLabel(entity.getApartment().getApartmentCode() + " - " + entity.getUser().getFullName());
        return dto;
    }
} 