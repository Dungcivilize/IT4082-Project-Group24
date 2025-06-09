package KTPM.Backend.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import KTPM.Backend.entity.PaymentPeriod;
import KTPM.Backend.entity.ApartmentOwnership;
import KTPM.Backend.service.PaymentPeriodService;
import KTPM.Backend.service.ApartmentOwnershipService;
import KTPM.Backend.dto.ApartmentOwnershipDTO;

@RestController
@RequestMapping("/api/accountant")
public class AccountantController {
    @Autowired
    private PaymentPeriodService paymentPeriodService;

    @Autowired
    private ApartmentOwnershipService apartmentOwnershipService;

    @GetMapping("/payment-periods")
    public ResponseEntity<List<PaymentPeriod>> getPaymentPeriods() {
        return ResponseEntity.ok(paymentPeriodService.getAllPaymentPeriods());
    }

    @GetMapping("/apartments")
    public ResponseEntity<List<ApartmentOwnershipDTO>> getApartments() {
        List<ApartmentOwnership> ownerships = apartmentOwnershipService.getAllOwnerships();
        List<ApartmentOwnershipDTO> dtos = ownerships.stream()
            .map(ApartmentOwnershipDTO::fromEntity)
            .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }
} 