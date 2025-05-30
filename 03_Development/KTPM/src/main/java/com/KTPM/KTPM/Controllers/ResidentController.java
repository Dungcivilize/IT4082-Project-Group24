package com.KTPM.KTPM.Controllers;

import com.KTPM.KTPM.DTO.ResidentDTO;
import com.KTPM.KTPM.Models.Resident;
import com.KTPM.KTPM.Repositories.ResidentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/residents")
public class ResidentController {

    @Autowired
    private ResidentRepository residentRepository;

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
}
