package com.KTPM.KTPM.Controllers;

import com.KTPM.KTPM.DTO.CitizenSummaryDTO;
import com.KTPM.KTPM.DTO.ResidentDTO;
import com.KTPM.KTPM.Models.Citizen;
import com.KTPM.KTPM.Models.Resident;
import com.KTPM.KTPM.Repositories.ResidentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.KTPM.KTPM.Repositories.CitizenRepository;

import java.util.List;

@RestController
@RequestMapping("/api/residents")
public class ResidentController {

    @Autowired
    private ResidentRepository residentRepository;

    @Autowired
    private CitizenRepository citizenRepository;

    @GetMapping
    public ResponseEntity<?> getAllResidents() {
        List<Resident> residents = residentRepository.findAll();
        List<ResidentDTO> dtos = residents.stream()
                .map(resident -> new ResidentDTO(
                        resident.getResidentId(),
                        resident.getHouseholder().getCitizenId(),
                        resident.getAddress()
                )).toList();

        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{residentId}/citizens")
    public ResponseEntity<List<CitizenSummaryDTO>> getCitizensByResidentId(@PathVariable Long residentId) {
        List<Citizen> citizens = citizenRepository.findByResidentId(residentId);

        // Chuyển List<Citizen> thành List<CitizenSummaryDTO>
        List<CitizenSummaryDTO> citizenSummaries = citizens.stream()
            .map(citizen -> new CitizenSummaryDTO(
                citizen.getUser().getUserId(),           
                citizen.getUser().getUsername(),
                citizen.getUser().getPhone()
            ))
            .toList();

        return ResponseEntity.ok(citizenSummaries);
    }

    @DeleteMapping("/{residentId}")
    public ResponseEntity<String> deleteResidentAndCitizens(@PathVariable Long residentId) {
        // Xóa các Citizen có residentId = residentId
        citizenRepository.deleteByResidentId(residentId);

        // Xóa Resident
        residentRepository.deleteById(residentId);

        return ResponseEntity.ok("Deleted resident and related citizens with residentId = " + residentId);
    }

}

