package KTPM.Backend.Admin.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import KTPM.Backend.Admin.dto.AdminUserDTO;
import KTPM.Backend.Admin.dto.AdminUserDTOrequest;
import KTPM.Backend.entity.User;
import KTPM.Backend.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class AdminUserService {

    private final UserRepository userRepository;

    // Chuyển entity sang DTO
    private AdminUserDTO toDTO(User user) {
        AdminUserDTO dto = new AdminUserDTO();
        dto.setUserId(user.getUserId());
        dto.setUsername(user.getUsername());
        dto.setFullName(user.getFullName());
        dto.setEmail(user.getEmail());
        dto.setPhone(user.getPhone());
        dto.setRole(user.getRole());
        return dto;
    }

    // Lấy tất cả người dùng
    public List<AdminUserDTO> getAllUsers() {
        return userRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    // Lấy user theo ID
    public AdminUserDTO getUserById(Integer id) {
        return userRepository.findById(id).map(this::toDTO).orElse(null);
    }

    // Tạo mới user (chưa mã hóa password ở đây)
    public AdminUserDTO createUser(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new RuntimeException("Username đã tồn tại");
        }
        User saved = userRepository.save(user);
        return toDTO(saved);
    }

    // Cập nhật user
    @Transactional
    public User updateUser(Integer userId, AdminUserDTOrequest dto) throws Exception {
        Optional<User> optionalUser = userRepository.findById(userId);

        if (!optionalUser.isPresent()) {
            throw new Exception("User không tồn tại với id: " + userId);
        }

        User user = optionalUser.get();

        // Update các trường theo DTO
        user.setUsername(dto.getUsername());
        user.setFullName(dto.getFullName());
        user.setEmail(dto.getEmail());
        user.setPhone(dto.getPhone());
        user.setRole(User.Role.valueOf(dto.getRole()));

        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            // Thường bạn cần mã hóa password trước khi lưu
            // Ví dụ: user.setPassword(passwordEncoder.encode(dto.getPassword()));
            user.setPassword(dto.getPassword());
        }

        return userRepository.save(user);
    }

    // Xóa user
    public void deleteUser(Integer id) {
        userRepository.deleteById(id);
    }

    // Lấy user chưa là chủ hộ căn hộ nào cả
    public List<AdminUserDTO> getUsersWithoutActiveOwnership() {
    List<User> users = userRepository.findUsersNotOwningAnyActiveApartment();
    return users.stream().map(this::toDTO).collect(Collectors.toList());
}


    
}