package KTPM.Backend.Admin.dto;

import KTPM.Backend.entity.Apartment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminApartmentDTO {
    private Integer apartmentId;
    private String apartmentCode;
    private Integer floor;
    private BigDecimal area;
    private Apartment.Status status;

    // Có thể thêm các trường từ quan hệ nếu cần
    // private List<ApartmentOwnershipDTO> ownerships;
}