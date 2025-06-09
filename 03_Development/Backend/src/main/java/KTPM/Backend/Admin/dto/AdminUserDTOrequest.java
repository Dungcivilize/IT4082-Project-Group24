package KTPM.Backend.Admin.dto;

import lombok.Data;

@Data
public class AdminUserDTOrequest {
    private String username;
    private String fullName;
    private String email;
    private String phone;
    private String role;
    private String password;
}
