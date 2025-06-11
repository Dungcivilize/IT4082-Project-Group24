package KTPM.Backend.Admin.dto;

import KTPM.Backend.entity.User.Role;
import lombok.Data;

@Data
public class AdminUserDTO {
    private Integer userId;
    private String username;
    private String fullName;
    private String email;
    private String phone;
    private Role role;
}