package KTPM.Backend.dto;

import KTPM.Backend.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponse {
    private Integer userId;
    private String email;
    private String fullName;
    private String phone;
    private User.UserRole role;
    private Integer apartmentId;
} 