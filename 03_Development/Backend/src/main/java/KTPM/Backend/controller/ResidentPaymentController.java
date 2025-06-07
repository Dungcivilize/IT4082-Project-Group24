package KTPM.Backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import KTPM.Backend.dto.ResidentPaymentDetailResponse;
import KTPM.Backend.dto.ResidentPaymentRequest;
import KTPM.Backend.service.ResidentPaymentService;

@RestController
@RequestMapping("/api/residents/payments")
public class ResidentPaymentController {
    @Autowired
    private ResidentPaymentService paymentService;

    @GetMapping("/pending")
    public ResponseEntity<List<ResidentPaymentDetailResponse>> getPendingPayments(
            @RequestParam Integer userId) {
        return ResponseEntity.ok(paymentService.getPendingPayments(userId));
    }

    @GetMapping("/history")
    public ResponseEntity<List<ResidentPaymentDetailResponse>> getPaymentHistory(
            @RequestParam Integer userId) {
        return ResponseEntity.ok(paymentService.getPaymentHistory(userId));
    }

    @PostMapping("/submit")
    public ResponseEntity<ResidentPaymentDetailResponse> submitPayment(
            @RequestParam Integer userId,
            @RequestBody ResidentPaymentRequest request) {
        return ResponseEntity.ok(paymentService.submitPayment(userId, request));
    }
} 