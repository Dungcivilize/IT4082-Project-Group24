package KTPM.Backend.dto;

import KTPM.Backend.entity.Vehicle;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResidentVehicleResponse {
    private Integer vehicleId;
    private String licensePlate;
    private Vehicle.VehicleType type;
    private String apartmentCode;
    private String ownerName;
    private Integer ownerId;
} 