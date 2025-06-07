package KTPM.Backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import KTPM.Backend.dto.ResidentVehicleRequest;
import KTPM.Backend.dto.ResidentVehicleResponse;
import KTPM.Backend.service.ResidentVehicleService;

@RestController
@RequestMapping("/api/residents/vehicles")
public class ResidentVehicleController {
    @Autowired
    private ResidentVehicleService vehicleService;

    @GetMapping
    public ResponseEntity<List<ResidentVehicleResponse>> getVehicles(
            @RequestParam Integer userId) {
        return ResponseEntity.ok(vehicleService.getVehicles(userId));
    }

    @PostMapping
    public ResponseEntity<ResidentVehicleResponse> createVehicle(
            @RequestParam Integer userId,
            @RequestBody ResidentVehicleRequest request) {
        return ResponseEntity.ok(vehicleService.createVehicle(userId, request));
    }

    @PutMapping("/{vehicleId}")
    public ResponseEntity<ResidentVehicleResponse> updateVehicle(
            @RequestParam Integer userId,
            @PathVariable Integer vehicleId,
            @RequestBody ResidentVehicleRequest request) {
        return ResponseEntity.ok(vehicleService.updateVehicle(userId, vehicleId, request));
    }

    @DeleteMapping("/{vehicleId}")
    public ResponseEntity<Void> deleteVehicle(
            @RequestParam Integer userId,
            @PathVariable Integer vehicleId) {
        vehicleService.deleteVehicle(userId, vehicleId);
        return ResponseEntity.ok().build();
    }
} 