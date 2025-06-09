package KTPM.Backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationResponse {
    private String token;
    private Integer userId;
    private String username;
    private String fullName;
    private String email;
    private String phone;
    private String role;
    private Integer ownershipId;
    private String apartmentCode;
} 