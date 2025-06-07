package KTPM.Backend.dto;

import KTPM.Backend.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponse {
    private String email;
    private String fullName;
    private User.UserRole role;
    private Integer apartmentId;
} 