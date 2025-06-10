package KTPM.Backend.Admin.controller;

import KTPM.Backend.Admin.dto.AdminApartmentDTO;
import KTPM.Backend.Admin.dto.AdminOwnershipDTO;
import KTPM.Backend.Admin.dto.AdminResidentDTO;
import KTPM.Backend.Admin.dto.AdminVehicleDTO;
import KTPM.Backend.Admin.service.AdminOwnershipService;
import KTPM.Backend.Admin.service.AdminResidentService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import KTPM.Backend.Admin.service.AdminVehicleService;
import KTPM.Backend.repository.ApartmentRepository;

import java.util.List;

@RestController
@RequestMapping("/api/ownerships")
@RequiredArgsConstructor
public class AdminOwnershipController {

    private final AdminOwnershipService ownershipService;
    private final AdminResidentService residentService;
    private final AdminVehicleService vehicleService;
    private final ApartmentRepository apartmentRepository;

    @GetMapping("/active")
    public ResponseEntity<List<AdminOwnershipDTO>> getActiveOwnerships() {
        List<AdminOwnershipDTO> list = ownershipService.getActiveOwnerships();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/empty")
    public ResponseEntity<List<AdminApartmentDTO>> getEmptyApartments() {
        List<AdminApartmentDTO> emptyApartments = apartmentRepository.findEmptyApartments();
        return ResponseEntity.ok(emptyApartments);
    }

    @GetMapping("/{ownershipId}/residents")
    public ResponseEntity<List<AdminResidentDTO>> getResidentsByOwnershipId(@PathVariable Integer ownershipId) {
        List<AdminResidentDTO> residents = residentService.getResidentsByOwnershipId(ownershipId);
        return ResponseEntity.ok(residents);
    }

        @GetMapping("/{ownershipId}/vehicles")
    public ResponseEntity<List<AdminVehicleDTO>> getVehiclesByOwnershipId(@PathVariable Integer ownershipId) {
        List<AdminVehicleDTO> vehicles = vehicleService.getVehiclesByOwnershipId(ownershipId);
        return ResponseEntity.ok(vehicles);
    }

    


}
