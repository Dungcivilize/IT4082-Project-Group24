package KTPM.Backend.Admin.controller;

import KTPM.Backend.Admin.dto.AdminApartmentDTO;
import KTPM.Backend.Admin.dto.AdminOwnershipDTO;
import KTPM.Backend.Admin.dto.AdminResidentDTO;
import KTPM.Backend.Admin.dto.AdminTemporaryAbsentDTO;
import KTPM.Backend.Admin.dto.AdminTemporaryResidentDTO;
import KTPM.Backend.Admin.dto.AdminVehicleDTO;
import KTPM.Backend.Admin.service.AdminOwnershipService;
import KTPM.Backend.Admin.service.AdminResidentService;
import KTPM.Backend.Admin.service.AdminTemporaryService;

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

    @PutMapping("/{ownershipId}")
    public ResponseEntity<AdminOwnershipDTO> updateOwnershipStatus(
            @PathVariable Integer ownershipId,
            @RequestBody AdminOwnershipDTO updatedOwnership) {
        AdminOwnershipDTO result = ownershipService.updateOwnershipStatus(ownershipId, updatedOwnership);
        return ResponseEntity.ok(result);
    }

    private final AdminTemporaryService temporaryService;

    @GetMapping("/{ownershipId}/temporary-residents")
    public ResponseEntity<List<AdminTemporaryResidentDTO>> getTemporaryResidents(@PathVariable Integer ownershipId) {
        List<AdminTemporaryResidentDTO> list = temporaryService.getTemporaryResidentsByOwnershipId(ownershipId);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{ownershipId}/temporary-absents")
    public ResponseEntity<List<AdminTemporaryAbsentDTO>> getTemporaryAbsents(@PathVariable Integer ownershipId) {
        List<AdminTemporaryAbsentDTO> list = temporaryService.getTemporaryAbsentsByOwnershipId(ownershipId);
        return ResponseEntity.ok(list);
    }
}
