package KTPM.Backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import KTPM.Backend.dto.UpdateProfileRequest;
import KTPM.Backend.dto.UserProfileResponse;
import KTPM.Backend.service.UserProfileService;

@RestController
@RequestMapping("/api/users/profile")
public class UserProfileController {
    @Autowired
    private UserProfileService userProfileService;

    @GetMapping("/{userId}")
    public ResponseEntity<?> getProfile(@PathVariable Integer userId) {
        try {
            UserProfileResponse response = userProfileService.getUserProfile(userId);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<?> updateProfile(
            @PathVariable Integer userId,
            @RequestBody UpdateProfileRequest request) {
        try {
            UserProfileResponse response = userProfileService.updateProfile(userId, request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
} 