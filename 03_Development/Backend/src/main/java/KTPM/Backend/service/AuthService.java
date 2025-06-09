package KTPM.Backend.service;

import KTPM.Backend.dto.LoginRequest;
import KTPM.Backend.dto.LoginResponse;
import KTPM.Backend.entity.User;
import KTPM.Backend.entity.ApartmentOwnership;
import KTPM.Backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AuthService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ApartmentOwnershipService apartmentOwnershipService;

    public LoginResponse login(LoginRequest request) {
        Optional<User> userOpt = userRepository.findByEmailAndPassword(request.getEmail(), request.getPassword());
        
        if (userOpt.isEmpty()) {
            throw new RuntimeException("Email hoặc mật khẩu không chính xác");
        }

        User user = userOpt.get();

        // Nếu là resident thì phải kiểm tra quyền sở hữu
        if (user.getRole() == User.Role.resident) {
            ApartmentOwnership ownership = null;
            try {
                ownership = apartmentOwnershipService.getCurrentOwnershipByUserId(user.getUserId());
                
                // Kiểm tra ownership có active không
                if (ownership.getStatus() != ApartmentOwnership.Status.active) {
                    throw new RuntimeException("Tài khoản của bạn không còn quyền sở hữu căn hộ nào");
                }
            } catch (RuntimeException e) {
                throw new RuntimeException("Tài khoản của bạn không còn quyền sở hữu căn hộ nào");
            }

            return new LoginResponse(
                user.getUserId(),
                user.getEmail(),
                user.getFullName(),
                user.getPhone(),
                user.getRole(),
                ownership.getOwnershipId()
            );
        }

        // Nếu là admin hoặc accountant thì không cần kiểm tra ownership
        return new LoginResponse(
            user.getUserId(),
            user.getEmail(),
            user.getFullName(),
            user.getPhone(),
            user.getRole(),
            null
        );
    }
} 