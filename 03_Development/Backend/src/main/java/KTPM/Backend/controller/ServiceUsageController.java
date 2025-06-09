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

import KTPM.Backend.dto.PaymentDetailRequest;
import KTPM.Backend.dto.ResidentPaymentDetailResponse;
import KTPM.Backend.entity.PaymentDetail;
import KTPM.Backend.service.ServiceUsageService;

@RestController
@RequestMapping("/api/accountant/service-usage")
public class ServiceUsageController {
    @Autowired
    private ServiceUsageService serviceUsageService;

    @PostMapping
    public ResponseEntity<ResidentPaymentDetailResponse> createPaymentDetail(@RequestBody PaymentDetailRequest request) {
        return ResponseEntity.ok(serviceUsageService.createPaymentDetail(request));
    }

    @GetMapping
    public ResponseEntity<List<ResidentPaymentDetailResponse>> getPaymentDetails(
            @RequestParam(required = false) Integer paymentPeriodId,
            @RequestParam(required = false) Integer ownershipId,
            @RequestParam(required = false) PaymentDetail.Status status) {
        return ResponseEntity.ok(serviceUsageService.getPaymentDetails(paymentPeriodId, ownershipId, status));
    }
} 