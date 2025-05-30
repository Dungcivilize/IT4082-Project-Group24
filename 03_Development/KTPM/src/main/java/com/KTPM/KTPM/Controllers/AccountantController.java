package com.KTPM.KTPM.Controllers;

import com.KTPM.KTPM.DTO.AccountantDTO;
import com.KTPM.KTPM.Models.Accountant;
import com.KTPM.KTPM.Repositories.AccountantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/accountants")
public class AccountantController {

    @Autowired
    private AccountantRepository accountantRepository;

    // Convert Accountant entity sang DTO
    private AccountantDTO convertToDTO(Accountant accountant) {
        return new AccountantDTO(accountant.getAccountantId(), accountant.getSalary());
    }

    // Lấy danh sách tất cả accountant (trả về List<AccountantDTO>)
    @GetMapping
    public List<AccountantDTO> getAllAccountants() {
        List<Accountant> accountants = accountantRepository.findAll();
        return accountants.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Lấy accountant theo ID (trả về AccountantDTO)
    @GetMapping("/{id}")
    public ResponseEntity<AccountantDTO> getAccountantById(@PathVariable Long id) {
        Optional<Accountant> accountant = accountantRepository.findById(id);
        return accountant
                .map(a -> ResponseEntity.ok(convertToDTO(a)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Tạo mới accountant
    @PostMapping
    public AccountantDTO createAccountant(@RequestBody Accountant accountant) {
        Accountant saved = accountantRepository.save(accountant);
        return convertToDTO(saved);
    }

    // Cập nhật accountant
    @PutMapping("/{id}")
    public ResponseEntity<AccountantDTO> updateAccountant(@PathVariable Long id, @RequestBody Accountant accountantDetails) {
        Optional<Accountant> optionalAccountant = accountantRepository.findById(id);
        if (optionalAccountant.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Accountant accountant = optionalAccountant.get();
        // Lưu ý: nếu bạn không muốn update user ở đây, bỏ dòng này đi
        accountant.setUser(accountantDetails.getUser());
        accountant.setSalary(accountantDetails.getSalary());

        Accountant updatedAccountant = accountantRepository.save(accountant);
        return ResponseEntity.ok(convertToDTO(updatedAccountant));
    }

    // Xóa accountant
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccountant(@PathVariable Long id) {
        if (!accountantRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        accountantRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
