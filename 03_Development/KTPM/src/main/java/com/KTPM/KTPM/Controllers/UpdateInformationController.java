package com.KTPM.KTPM.Controllers;

import com.KTPM.KTPM.DTO.CitizenUpdateRequest;
import com.KTPM.KTPM.Models.Citizen;
import com.KTPM.KTPM.Models.User;
import com.KTPM.KTPM.Repositories.CitizenRepository;
import com.KTPM.KTPM.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/citizens")
public class UpdateInformationController {

    @Autowired
    private CitizenRepository citizenRepository;

    @Autowired
    private UserRepository userRepository;

    @PatchMapping("/{id}")
    public ResponseEntity<String> updateCitizenAndUser(@PathVariable Long id, @RequestBody CitizenUpdateRequest updateRequest) {
        Optional<Citizen> optionalCitizen = citizenRepository.findById(id);
        if (optionalCitizen.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Citizen citizen = optionalCitizen.get();

        // Cập nhật Citizen
        if (updateRequest.getCccdId() != null)
            citizen.setCccdId(updateRequest.getCccdId());
        if (updateRequest.getJob() != null)
            citizen.setJob(updateRequest.getJob());
        if (updateRequest.getDob() != null)
            citizen.setDob(updateRequest.getDob());

        // Cập nhật User liên kết
        User user = citizen.getUser();
        if (updateRequest.getPhone() != null)
            user.setPhone(updateRequest.getPhone());
        if (updateRequest.getFullname() != null)
            user.setFullname(updateRequest.getFullname());
        if (updateRequest.getEmail() != null)
            user.setEmail(updateRequest.getEmail());

        // Lưu lại
        userRepository.save(user);
        citizenRepository.save(citizen);

        return ResponseEntity.ok("Cập nhật thành công");
    }
}

