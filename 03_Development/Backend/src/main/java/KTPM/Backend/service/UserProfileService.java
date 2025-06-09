package KTPM.Backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import KTPM.Backend.dto.UpdateProfileRequest;
import KTPM.Backend.dto.UserProfileResponse;
import KTPM.Backend.entity.User;
import KTPM.Backend.entity.ApartmentOwnership;
import KTPM.Backend.repository.UserRepository;

@Service
public class UserProfileService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ApartmentOwnershipService apartmentOwnershipService;

    public UserProfileResponse getUserProfile(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));

        ApartmentOwnership ownership = null;
        try {
            ownership = apartmentOwnershipService.getCurrentOwnershipByUserId(userId);
        } catch (RuntimeException e) {
            // Không có ownership hiện tại - có thể là admin hoặc accountant
        }

        return new UserProfileResponse(
            user.getUserId(),
            user.getUsername(),
            user.getEmail(),
            user.getFullName(),
            user.getPhone(),
            user.getRole(),
            ownership != null ? ownership.getApartment().getApartmentCode() : null,
            ownership != null ? ownership.getOwnershipId() : null
        );
    }

    public UserProfileResponse updateProfile(Integer userId, UpdateProfileRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));

        // Chỉ cập nhật mật khẩu nếu có currentPassword và newPassword
        if (request.getNewPassword() != null && !request.getNewPassword().trim().isEmpty()) {
            // Phải cung cấp mật khẩu hiện tại để đổi mật khẩu
            if (request.getCurrentPassword() == null || request.getCurrentPassword().trim().isEmpty()) {
                throw new RuntimeException("Vui lòng nhập mật khẩu hiện tại để đổi mật khẩu");
            }
            
            if (!user.getPassword().equals(request.getCurrentPassword())) {
                throw new RuntimeException("Mật khẩu hiện tại không chính xác");
            }
            user.setPassword(request.getNewPassword().trim());
        }

        // Chỉ cập nhật email nếu có giá trị mới và khác null
        if (request.getEmail() != null && !request.getEmail().trim().isEmpty()) {
            user.setEmail(request.getEmail().trim());
        }

        // Chỉ cập nhật số điện thoại nếu có giá trị mới và khác null
        if (request.getPhone() != null && !request.getPhone().trim().isEmpty()) {
            user.setPhone(request.getPhone().trim());
        }

        user = userRepository.save(user);

        ApartmentOwnership ownership = null;
        try {
            ownership = apartmentOwnershipService.getCurrentOwnershipByUserId(userId);
        } catch (RuntimeException e) {
            // Không có ownership hiện tại - có thể là admin hoặc accountant
        }

        return new UserProfileResponse(
            user.getUserId(),
            user.getUsername(),
            user.getEmail(),
            user.getFullName(),
            user.getPhone(),
            user.getRole(),
            ownership != null ? ownership.getApartment().getApartmentCode() : null,
            ownership != null ? ownership.getOwnershipId() : null
        );
    }
} 