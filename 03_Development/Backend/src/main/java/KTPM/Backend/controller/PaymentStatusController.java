package KTPM.Backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import KTPM.Backend.dto.PaymentStatusUpdateRequest;
import KTPM.Backend.dto.ResidentPaymentDetailResponse;
import KTPM.Backend.entity.PaymentDetail;
import KTPM.Backend.service.PaymentStatusService;
import KTPM.Backend.service.PaymentPeriodService;
import KTPM.Backend.service.ApartmentOwnershipService;
import KTPM.Backend.entity.PaymentPeriod;
import KTPM.Backend.entity.ApartmentOwnership;

@RestController
@RequestMapping("/api/accountant/payment-status")
public class PaymentStatusController {
    @Autowired
    private PaymentStatusService paymentStatusService;

    @Autowired
    private PaymentPeriodService paymentPeriodService;

    @Autowired
    private ApartmentOwnershipService apartmentOwnershipService;

    @GetMapping
    public ResponseEntity<List<ResidentPaymentDetailResponse>> getAllPayments(
            @RequestParam(required = false) Integer paymentPeriodId,
            @RequestParam(required = false) Integer ownershipId) {
        return ResponseEntity.ok(paymentStatusService.getAllPayments(paymentPeriodId, ownershipId));
    }

    @GetMapping("/by-status")
    public ResponseEntity<List<ResidentPaymentDetailResponse>> getPaymentsByStatus(
            @RequestParam PaymentDetail.Status status,
            @RequestParam(required = false) Integer paymentPeriodId,
            @RequestParam(required = false) Integer ownershipId) {
        return ResponseEntity.ok(paymentStatusService.getPaymentsByStatus(status, paymentPeriodId, ownershipId));
    }

    @GetMapping("/payment-periods")
    public ResponseEntity<List<PaymentPeriod>> getPaymentPeriods() {
        return ResponseEntity.ok(paymentPeriodService.getAllPaymentPeriods());
    }

    @GetMapping("/apartments")
    public ResponseEntity<List<ApartmentOwnership>> getApartments() {
        return ResponseEntity.ok(apartmentOwnershipService.getAllOwnerships());
    }

    @PutMapping("/update")
    public ResponseEntity<ResidentPaymentDetailResponse> updatePaymentStatus(
            @RequestBody PaymentStatusUpdateRequest request) {
        return ResponseEntity.ok(paymentStatusService.updatePaymentStatus(request));
    }
} 