package KTPM.Backend.dto;

import KTPM.Backend.entity.Vehicle;
import lombok.Data;

@Data
public class ResidentVehicleRequest {
    private String licensePlate;
    private Vehicle.VehicleType type;
} 