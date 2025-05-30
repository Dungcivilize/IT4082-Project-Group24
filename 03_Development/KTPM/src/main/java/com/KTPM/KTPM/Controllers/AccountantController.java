package com.KTPM.KTPM.Controllers;

import com.KTPM.KTPM.Models.Accountant;
import com.KTPM.KTPM.Repositories.AccountantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/accountants")
public class AccountantController {

    @Autowired
    private AccountantRepository accountantRepository;

    @GetMapping("/by-user/{userId}")
    public ResponseEntity<Long> getAccountantIdByUserId(@PathVariable Long userId) {
        Accountant accountant = accountantRepository.findByUser_UserId(userId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy accountant với user ID: " + userId));
        return ResponseEntity.ok(accountant.getAccountantId());
    }
}
