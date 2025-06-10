package KTPM.Backend.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import KTPM.Backend.dto.PaymentStatusUpdateRequest;
import KTPM.Backend.dto.PaymentDetailUpdateRequest;
import KTPM.Backend.dto.ResidentPaymentDetailResponse;
import KTPM.Backend.dto.ApartmentOwnershipSelectDTO;
import KTPM.Backend.entity.PaymentDetail;
import KTPM.Backend.service.PaymentStatusService;
import KTPM.Backend.service.PaymentPeriodService;
import KTPM.Backend.service.ApartmentOwnershipService;
import KTPM.Backend.entity.PaymentPeriod;
import KTPM.Backend.entity.ApartmentOwnership;

@RestController
@RequestMapping("/api")
public class PaymentStatusController {
    @Autowired
    private PaymentStatusService paymentStatusService;

    @Autowired
    private PaymentPeriodService paymentPeriodService;

    @Autowired
    private ApartmentOwnershipService apartmentOwnershipService;

    // API cho cư dân
    @GetMapping("/resident/payment-status/{ownershipId}")
    public ResponseEntity<List<ResidentPaymentDetailResponse>> getResidentPayments(
            @PathVariable Integer ownershipId) {
        return ResponseEntity.ok(paymentStatusService.getResidentPayments(ownershipId));
    }

    @GetMapping("/resident/payment-status/{ownershipId}/by-status")
    public ResponseEntity<List<ResidentPaymentDetailResponse>> getResidentPaymentsByStatus(
            @PathVariable Integer ownershipId,
            @RequestParam PaymentDetail.Status status) {
        return ResponseEntity.ok(paymentStatusService.getResidentPaymentsByStatus(ownershipId, status));
    }

    // API cho kế toán
    @GetMapping("/accountant/payment-status")
    public ResponseEntity<List<ResidentPaymentDetailResponse>> getAccountantPayments(
            @RequestParam(required = false) Integer paymentPeriodId,
            @RequestParam(required = false) String ownershipIds) {
        return ResponseEntity.ok(paymentStatusService.getAccountantPayments(paymentPeriodId, ownershipIds));
    }

    @GetMapping("/accountant/payment-status/by-status")
    public ResponseEntity<List<ResidentPaymentDetailResponse>> getAccountantPaymentsByStatus(
            @RequestParam PaymentDetail.Status status,
            @RequestParam(required = false) Integer paymentPeriodId,
            @RequestParam(required = false) String ownershipIds) {
        return ResponseEntity.ok(paymentStatusService.getAccountantPaymentsByStatus(status, paymentPeriodId, ownershipIds));
    }

    @GetMapping("/accountant/payment-status/payment-periods")
    public ResponseEntity<List<PaymentPeriod>> getPaymentPeriods() {
        return ResponseEntity.ok(paymentPeriodService.getAllPaymentPeriods());
    }

    @GetMapping("/accountant/payment-status/apartments")
    public ResponseEntity<List<ApartmentOwnershipSelectDTO>> getApartments() {
        try {
            List<ApartmentOwnership> ownerships = apartmentOwnershipService.getLatestOwnerships();
            List<ApartmentOwnershipSelectDTO> dtos = ownerships.stream()
                .map(ApartmentOwnershipSelectDTO::fromEntity)
                .collect(Collectors.toList());
            return ResponseEntity.ok(dtos);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/accountant/payment-status/apartments/{apartmentId}/ownerships")
    public ResponseEntity<List<ApartmentOwnershipSelectDTO>> getApartmentOwnerships(
            @PathVariable Integer apartmentId) {
        try {
            List<ApartmentOwnership> ownerships = apartmentOwnershipService.getOwnershipsByApartmentId(apartmentId);
            List<ApartmentOwnershipSelectDTO> dtos = ownerships.stream()
                .map(ApartmentOwnershipSelectDTO::fromEntity)
                .collect(Collectors.toList());
            return ResponseEntity.ok(dtos);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping("/accountant/payment-status/update")
    public ResponseEntity<ResidentPaymentDetailResponse> updatePaymentStatus(
            @RequestBody PaymentStatusUpdateRequest request) {
        return ResponseEntity.ok(paymentStatusService.updatePaymentStatus(request));
    }

    @PatchMapping("/accountant/payment-status/update-detail")
    public ResponseEntity<ResidentPaymentDetailResponse> updatePaymentDetail(
            @RequestBody PaymentDetailUpdateRequest request) {
        return ResponseEntity.ok(paymentStatusService.updatePaymentDetail(request));
    }
} 