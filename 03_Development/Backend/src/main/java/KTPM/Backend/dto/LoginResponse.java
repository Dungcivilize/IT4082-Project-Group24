package KTPM.Backend.dto;

import KTPM.Backend.entity.User.Role;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponse {
    private Integer userId;
    private String email;
    private String fullName;
    private String phone;
    private Role role;
    private Integer ownershipId;
} 