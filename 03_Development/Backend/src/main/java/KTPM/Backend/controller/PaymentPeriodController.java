package KTPM.Backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import KTPM.Backend.dto.PaymentPeriodDTO;
import KTPM.Backend.entity.PaymentPeriod;
import KTPM.Backend.service.PaymentPeriodService;

@RestController
@RequestMapping("/api/payment-periods")
@CrossOrigin(origins = "http://localhost:5173")
public class PaymentPeriodController {
    @Autowired
    private PaymentPeriodService paymentPeriodService;

    @GetMapping
    public ResponseEntity<List<PaymentPeriodDTO>> getAllPaymentPeriods() {
        return ResponseEntity.ok(paymentPeriodService.getAllPaymentPeriodsWithStatus());
    }

    @PostMapping
    public ResponseEntity<?> createPaymentPeriod(@RequestBody PaymentPeriodDTO dto) {
        try {
            // Validate input
            if (dto.getMonth() == null || dto.getYear() == null) {
                return ResponseEntity.badRequest().body("Tháng và năm không được để trống");
            }
            if (dto.getMonth() < 1 || dto.getMonth() > 12) {
                return ResponseEntity.badRequest().body("Tháng phải từ 1 đến 12");
            }
            if (dto.getYear() < 2000) {
                return ResponseEntity.badRequest().body("Năm phải từ 2000 trở lên");
            }

            // Convert DTO to entity
            PaymentPeriod paymentPeriod = new PaymentPeriod();
            paymentPeriod.setMonth(dto.getMonth());
            paymentPeriod.setYear(dto.getYear());

            // Create payment period
            PaymentPeriod created = paymentPeriodService.createPaymentPeriod(paymentPeriod);

            // Convert back to DTO
            PaymentPeriodDTO createdDto = new PaymentPeriodDTO(
                created.getPaymentPeriodId(),
                created.getMonth(),
                created.getYear(),
                false // New payment period is never completed
            );

            return ResponseEntity.ok(createdDto);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
} 