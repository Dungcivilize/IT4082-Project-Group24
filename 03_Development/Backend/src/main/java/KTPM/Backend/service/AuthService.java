package KTPM.Backend.service;

import KTPM.Backend.dto.LoginRequest;
import KTPM.Backend.dto.LoginResponse;
import KTPM.Backend.entity.User;
import KTPM.Backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {
    @Autowired
    private UserRepository userRepository;

    public LoginResponse login(LoginRequest request) {
        Optional<User> userOpt = userRepository.findByEmailAndPassword(request.getEmail(), request.getPassword());
        
        if (userOpt.isEmpty()) {
            throw new RuntimeException("Email hoặc mật khẩu không chính xác");
        }

        User user = userOpt.get();
        return new LoginResponse(
            user.getEmail(),
            user.getFullName(),
            user.getRole(),
            user.getApartment() != null ? user.getApartment().getApartmentId() : null
        );
    }
} 