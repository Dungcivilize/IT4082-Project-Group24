package com.KTPM.KTPM.Controllers;

import com.KTPM.KTPM.DTO.PaymentDTO;
import com.KTPM.KTPM.Models.Payment;
import com.KTPM.KTPM.Models.Payment.PaymentStatus;
import com.KTPM.KTPM.Repositories.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    @Autowired
    private PaymentRepository paymentRepository;

    // Lấy các payment đã thanh toán
    @GetMapping("/citizen/{citizenId}/paid")
    public ResponseEntity<List<PaymentDTO>> getPaidPaymentsByCitizenId(@PathVariable Long citizenId) {
        List<Payment> payments = paymentRepository.findByCitizen_CitizenIdAndStatus(citizenId, PaymentStatus.PAID);
        List<PaymentDTO> paymentDTOs = payments.stream()
                .map(PaymentDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(paymentDTOs);
    }

    // Lấy các payment chưa thanh toán
    @GetMapping("/citizen/{citizenId}/unpaid")
    public ResponseEntity<List<PaymentDTO>> getUnpaidPaymentsByCitizenId(@PathVariable Long citizenId) {
        List<Payment> payments = paymentRepository.findByCitizen_CitizenIdAndStatus(citizenId, PaymentStatus.UNPAID);
        List<PaymentDTO> paymentDTOs = payments.stream()
                .map(PaymentDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(paymentDTOs);
    }

    // Lấy tất cả khoản nộp
    @GetMapping
    public ResponseEntity<List<PaymentDTO>> getAllPayments() {
        List<PaymentDTO> result = paymentRepository.findAll()
                .stream().map(PaymentDTO::new).collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    // Lấy chi tiết khoản nộp theo ID
    @GetMapping("/{id}")
    public ResponseEntity<PaymentDTO> getPaymentById(@PathVariable Long id) {
        return paymentRepository.findById(id)
                .map(payment -> ResponseEntity.ok(new PaymentDTO(payment)))
                .orElse(ResponseEntity.notFound().build());
    }

    // Tạo khoản nộp mới
    @PostMapping
    public ResponseEntity<Payment> createPayment(@RequestBody Payment payment) {
        Payment saved = paymentRepository.save(payment);
        return ResponseEntity.ok(saved);
    }

    // Cập nhật khoản nộp
    @PutMapping("/{id}")
    public ResponseEntity<Payment> updatePayment(@PathVariable Long id, @RequestBody Payment updatedPayment) {
        return paymentRepository.findById(id).map(existing -> {
            existing.setCitizen(updatedPayment.getCitizen());
            existing.setCollectionPeriod(updatedPayment.getCollectionPeriod());
            existing.setCost(updatedPayment.getCost());
            existing.setStatus(updatedPayment.getStatus());
            return ResponseEntity.ok(paymentRepository.save(existing));
        }).orElse(ResponseEntity.notFound().build());
    }

    // Xoá khoản nộp
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePayment(@PathVariable Long id) {
        if (!paymentRepository.existsById(id)) return ResponseEntity.notFound().build();
        paymentRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

}