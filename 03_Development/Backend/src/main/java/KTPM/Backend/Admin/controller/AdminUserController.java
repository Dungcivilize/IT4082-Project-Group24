package KTPM.Backend.Admin.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import KTPM.Backend.Admin.dto.AdminUserDTO;
import KTPM.Backend.Admin.dto.AdminUserDTOrequest;
import KTPM.Backend.entity.User;
import KTPM.Backend.Admin.service.AdminUserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class AdminUserController {

    private final AdminUserService userService;

    // Lấy tất cả user
    @GetMapping
    public List<AdminUserDTO> getAllUsers() {
        return userService.getAllUsers();
    }

    // Lấy user theo ID
    @GetMapping("/{id}")
    public ResponseEntity<AdminUserDTO> getUser(@PathVariable Integer id) {
        AdminUserDTO user = userService.getUserById(id);
        if (user == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(user);
    }

    // Tạo user mới
    @PostMapping
    public ResponseEntity<AdminUserDTO> createUser(@RequestBody User user) {
        try {
            AdminUserDTO created = userService.createUser(user);
            return new ResponseEntity<>(created, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Cập nhật user
    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(
            @PathVariable("id") Integer userId,
            @RequestBody AdminUserDTOrequest dto
    ) {
        try {
            User updatedUser = userService.updateUser(userId, dto);
            return ResponseEntity.ok(updatedUser);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Xóa user
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/no-active-ownership")
public ResponseEntity<List<AdminUserDTO>> getUsersWithoutActiveOwnership() {
    List<AdminUserDTO> users = userService.getUsersWithoutActiveOwnership();
    return ResponseEntity.ok(users);
}


}