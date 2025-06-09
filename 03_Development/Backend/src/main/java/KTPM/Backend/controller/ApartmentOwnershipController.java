package KTPM.Backend.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import KTPM.Backend.dto.ApartmentOwnershipDTO;
import KTPM.Backend.dto.CreateOwnershipRequest;
import KTPM.Backend.dto.EndOwnershipRequest;
import KTPM.Backend.entity.ApartmentOwnership;
import KTPM.Backend.service.ApartmentOwnershipService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/ownerships")
@CrossOrigin(origins = "http://localhost:5173")
public class ApartmentOwnershipController {
    @Autowired
    private ApartmentOwnershipService ownershipService;

    @GetMapping
    public ResponseEntity<List<ApartmentOwnershipDTO>> getAllOwnerships() {
        try {
            List<ApartmentOwnership> ownerships = ownershipService.getAllOwnerships();
            List<ApartmentOwnershipDTO> dtos = ownerships.stream()
                .map(ApartmentOwnershipDTO::fromEntity)
                .collect(Collectors.toList());
            return ResponseEntity.ok(dtos);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApartmentOwnershipDTO> getOwnership(@PathVariable Integer id) {
        ApartmentOwnership ownership = ownershipService.getOwnershipById(id);
        return ResponseEntity.ok(ApartmentOwnershipDTO.fromEntity(ownership));
    }

    @GetMapping("/by-apartment/{apartmentId}")
    public ResponseEntity<List<ApartmentOwnershipDTO>> getOwnershipsByApartment(@PathVariable Integer apartmentId) {
        List<ApartmentOwnership> ownerships = ownershipService.getOwnershipsByApartmentId(apartmentId);
        List<ApartmentOwnershipDTO> dtos = ownerships.stream()
                .map(ApartmentOwnershipDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/by-user/{userId}")
    public ResponseEntity<List<ApartmentOwnershipDTO>> getOwnershipsByUser(@PathVariable Integer userId) {
        List<ApartmentOwnership> ownerships = ownershipService.getOwnershipsByUserId(userId);
        List<ApartmentOwnershipDTO> dtos = ownerships.stream()
                .map(ApartmentOwnershipDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @PostMapping
    public ResponseEntity<ApartmentOwnershipDTO> createOwnership(@Valid @RequestBody CreateOwnershipRequest request) {
        ApartmentOwnership ownership = ownershipService.assignApartmentToUser(
                request.getApartmentId(),
                request.getUserId(),
                request.getStartDate());
        return ResponseEntity.ok(ApartmentOwnershipDTO.fromEntity(ownership));
    }

    @PostMapping("/{id}/end")
    public ResponseEntity<Void> endOwnership(@PathVariable Integer id, @Valid @RequestBody EndOwnershipRequest request) {
        ownershipService.endOwnership(id, request.getEndDate());
        return ResponseEntity.ok().build();
    }
} 