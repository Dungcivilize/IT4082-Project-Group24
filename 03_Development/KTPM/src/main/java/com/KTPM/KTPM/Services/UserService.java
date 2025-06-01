package com.KTPM.KTPM.Services;

import com.KTPM.KTPM.DTO.UserDTO;
import com.KTPM.KTPM.DTO.UserRequestDTO;
import com.KTPM.KTPM.Models.Role;
import com.KTPM.KTPM.Models.User;
import com.KTPM.KTPM.Repositories.RoleRepository;
import com.KTPM.KTPM.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    public List<UserDTO> getAllUsers() {
        return userRepository.findAll()
                .stream().map(UserDTO::new)
                .collect(Collectors.toList());
    }

    public UserDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return new UserDTO(user);
    }

    public UserDTO createUser(UserRequestDTO request) {
        Role role = roleRepository.findByRoleName(request.getRoleName())
                .orElseThrow(() -> new RuntimeException("Role not found"));

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword()); // có thể mã hóa nếu cần
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setFullname(request.getFullname());
        user.setRole(role);
        user.setCreatedAt(LocalDateTime.now());

        return new UserDTO(userRepository.save(user));
    }

    public UserDTO updateUser(Long id, UserRequestDTO request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Role role = roleRepository.findByRoleName(request.getRoleName())
        .orElseThrow(() -> new RuntimeException("Role not found"));

        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setFullname(request.getFullname());
        user.setRole(role);
        user.setUpdatedAt(LocalDateTime.now());

        return new UserDTO(userRepository.save(user));
    }

    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found");
        }
        userRepository.deleteById(id);
    }
}
