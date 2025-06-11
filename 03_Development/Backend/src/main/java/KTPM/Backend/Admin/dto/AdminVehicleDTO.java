package KTPM.Backend.Admin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminVehicleDTO {
    private Integer vehicleId;
    private String vehicleOwnerName;
    private String type;
    private String licensePlate;
}
