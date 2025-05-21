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

}