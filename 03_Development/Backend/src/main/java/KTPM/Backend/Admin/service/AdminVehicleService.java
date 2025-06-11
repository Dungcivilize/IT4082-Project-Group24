package KTPM.Backend.Admin.service;

import KTPM.Backend.Admin.dto.AdminVehicleDTO;
import KTPM.Backend.entity.Vehicle;
import KTPM.Backend.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminVehicleService {

    private final VehicleRepository vehicleRepository;

        public List<AdminVehicleDTO> getVehiclesByOwnershipId(Integer ownershipId) {
    List<Vehicle> vehicles = vehicleRepository.findAllByOwnership_OwnershipId(ownershipId);

    return vehicles.stream()
            .map(v -> new AdminVehicleDTO(
                    v.getVehicleId(),
                    v.getOwner().getFullName(), // ✅ đúng với kiểu String
                    v.getType().name(),         // enum -> String
                    v.getLicensePlate()
            ))
            .collect(Collectors.toList());
}


}
