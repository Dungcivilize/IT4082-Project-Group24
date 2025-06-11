package KTPM.Backend.Admin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import KTPM.Backend.entity.ApartmentOwnership;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminOwnershipDTO {
    private Integer ownershipId;
    private String ownershipName;
    private Integer apartmentId;
    private String apartmentCode;
    private Integer floor;
    private ApartmentOwnership.Status ownershipStatus;
}
