package KTPM.Backend.dto;

import KTPM.Backend.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileResponse {
    private Integer userId;
    private String username;
    private String email;
    private String fullName;
    private String phone;
    private User.UserRole role;
    private String apartmentCode;
    private Integer apartmentId;
} 