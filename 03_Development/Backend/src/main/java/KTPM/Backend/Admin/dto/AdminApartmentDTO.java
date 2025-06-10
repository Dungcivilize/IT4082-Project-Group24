package KTPM.Backend.Admin.dto;

import lombok.Data;

@Data
public class AdminApartmentDTO {
    private Integer apartmentId;
    private String apartmentCode;
    private Integer floor;

    public AdminApartmentDTO(Integer apartmentId, String apartmentCode, Integer floor) {
        this.apartmentId = apartmentId;
        this.apartmentCode = apartmentCode;
        this.floor = floor;
    }
}
