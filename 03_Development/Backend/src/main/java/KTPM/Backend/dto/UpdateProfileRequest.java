package KTPM.Backend.dto;

import lombok.Data;

@Data
public class UpdateProfileRequest {
    private String email;
    private String phone;
    private String currentPassword;
    private String newPassword;
} 