package com.KTPM.KTPM.Services;

import com.KTPM.KTPM.DTO.LoginResponse;
import com.KTPM.KTPM.Models.User;
import com.KTPM.KTPM.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    @Autowired
    private UserRepository userRepository;


    public LoginResponse login(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!user.getPassword().equals(password)) {
            throw new RuntimeException("Invalid password");
        }

        return new LoginResponse(
                "Login successful",
                user.getUserId(),
                user.getUsername(),
                user.getRole().getRoleId(),
                user.getFullname()
        );
    }
}