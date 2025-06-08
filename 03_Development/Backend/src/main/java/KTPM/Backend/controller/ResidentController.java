package KTPM.Backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import KTPM.Backend.dto.ResidentRequest;
import KTPM.Backend.dto.ResidentResponse;
import KTPM.Backend.service.ResidentService;

@RestController
@RequestMapping("/api/residents")
public class ResidentController {
    @Autowired
    private ResidentService residentService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getResidentsByUser(@PathVariable Integer userId) {
        try {
            List<ResidentResponse> residents = residentService.getResidentsByUser(userId);
            return ResponseEntity.ok(residents);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/user/addresident/{userId}")
    public ResponseEntity<?> addResident(
            @PathVariable Integer userId,
            @RequestBody ResidentRequest request) {
        try {
            ResidentResponse resident = residentService.addResident(userId, request);
            return ResponseEntity.ok(resident);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PatchMapping("/user/{userId}/resident/{residentId}")
    public ResponseEntity<?> updateResident(
            @PathVariable Integer userId,
            @PathVariable Integer residentId,
            @RequestBody ResidentRequest request) {
        try {
            ResidentResponse resident = residentService.updateResident(userId, residentId, request);
            return ResponseEntity.ok(resident);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/user/{userId}/resident/{residentId}")
    public ResponseEntity<?> deleteResident(
            @PathVariable Integer userId,
            @PathVariable Integer residentId) {
        try {
            residentService.deleteResident(userId, residentId);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
} 