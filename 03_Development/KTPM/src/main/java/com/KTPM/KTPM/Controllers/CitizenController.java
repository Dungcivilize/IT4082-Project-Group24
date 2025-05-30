package com.KTPM.KTPM.Controllers;

import com.KTPM.KTPM.DTO.CitizenDetailResponse;
import com.KTPM.KTPM.DTO.CitizenSummaryDTO;
import com.KTPM.KTPM.Models.Citizen;
import com.KTPM.KTPM.Models.Role;
import com.KTPM.KTPM.Models.User;
import com.KTPM.KTPM.Repositories.CitizenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/citizens")
public class CitizenController {
    @Autowired
    private CitizenRepository citizenRepository;

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getCitizenByUserId(@PathVariable Long userId) {
        Optional<Citizen> citizenOpt = citizenRepository.findByUser_UserId(userId);
        if (citizenOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Citizen citizen = citizenOpt.get();
        User user = citizen.getUser();
        Role role = user.getRole();

        CitizenDetailResponse dto = new CitizenDetailResponse(
                citizen.getCitizenId(),
                citizen.getCccdId(),
                citizen.getJob(),
                citizen.getDob(),
                user.getPhone(),
                user.getEmail(),
                user.getFullname(),
                user.getUsername(),
                role.getRoleId(),
                role.getRoleName(),
                citizen.getResidentId()
        );
        return ResponseEntity.ok(dto);
    }

    @GetMapping
    public ResponseEntity<?> getAllCitizens() {
        List<Citizen> citizens = citizenRepository.findAll();

        List<CitizenSummaryDTO> summaries = citizens.stream()
                .map(citizen -> new CitizenSummaryDTO(
                        citizen.getCitizenId(),
                        citizen.getUser() != null ? citizen.getUser().getFullname() : null,
                        citizen.getUser() != null ? citizen.getUser().getPhone() : null
                ))
                .toList();

        return ResponseEntity.ok(summaries);
    }
}
