package KTPM.Backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import KTPM.Backend.dto.ResidentTemporaryAbsentRequest;
import KTPM.Backend.dto.ResidentTemporaryAbsentResponse;
import KTPM.Backend.dto.ResidentTemporaryResidentRequest;
import KTPM.Backend.dto.ResidentTemporaryResidentResponse;
import KTPM.Backend.service.ResidentTemporaryAbsentService;
import KTPM.Backend.service.ResidentTemporaryResidentService;

@RestController
@RequestMapping("/api/residents/temporary")
public class ResidentTemporaryController {
    @Autowired
    private ResidentTemporaryResidentService temporaryResidentService;

    @Autowired
    private ResidentTemporaryAbsentService temporaryAbsentService;

    // Tạm trú
    @GetMapping("/residents")
    public ResponseEntity<List<ResidentTemporaryResidentResponse>> getTemporaryResidents(
            @RequestParam Integer userId) {
        return ResponseEntity.ok(temporaryResidentService.getTemporaryResidents(userId));
    }

    @PostMapping("/residents")
    public ResponseEntity<ResidentTemporaryResidentResponse> registerTemporaryResident(
            @RequestParam Integer userId,
            @RequestBody ResidentTemporaryResidentRequest request) {
        return ResponseEntity.ok(temporaryResidentService.registerTemporaryResident(userId, request));
    }

    // Tạm vắng
    @GetMapping("/absents")
    public ResponseEntity<List<ResidentTemporaryAbsentResponse>> getTemporaryAbsents(
            @RequestParam Integer userId) {
        return ResponseEntity.ok(temporaryAbsentService.getTemporaryAbsents(userId));
    }

    @PostMapping("/absents/{residentId}")
    public ResponseEntity<ResidentTemporaryAbsentResponse> registerTemporaryAbsent(
            @RequestParam Integer userId,
            @PathVariable Integer residentId,
            @RequestBody ResidentTemporaryAbsentRequest request) {
        return ResponseEntity.ok(temporaryAbsentService.registerTemporaryAbsent(userId, residentId, request));
    }

    @DeleteMapping("/absents/{temporaryAbsentId}")
    public ResponseEntity<Void> deleteTemporaryAbsent(
            @RequestParam Integer userId,
            @PathVariable Integer temporaryAbsentId) {
        temporaryAbsentService.deleteTemporaryAbsent(userId, temporaryAbsentId);
        return ResponseEntity.ok().build();
    }
} 