package KTPM.Backend.accountant.controller;

import KTPM.Backend.accountant.dto.*;
import KTPM.Backend.accountant.service.AccountantPaymentPeriodService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accountant/payment-periods")
@RequiredArgsConstructor
public class AccountantPaymentPeriodController {

    private final AccountantPaymentPeriodService service;
    private final AccountantPaymentPeriodService paymentPeriodService;


    @GetMapping
    public List<PaymentPeriodBasicDTO> getAllPaymentPeriods() {
        return service.getAllPaymentPeriods();
    }

    @GetMapping("/{id}/details")
    public PaymentPeriodWithApartmentsDTO getPaymentPeriodDetails(@PathVariable Long id) {
        return service.getPaymentPeriodDetails(id);
    }

    @PostMapping
    public ResponseEntity<PaymentPeriodBasicDTO> createPaymentPeriod(@RequestBody CreatePaymentPeriodRequestDTO request) {
        var createdPeriod = service.createPaymentPeriodWithDetails(request.getMonth(), request.getYear(), request.getNote());

        PaymentPeriodBasicDTO dto = new PaymentPeriodBasicDTO(
                createdPeriod.getPaymentPeriodId(),
                createdPeriod.getMonth(),
                createdPeriod.getYear()
        );

        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    @PutMapping("/payment-details/{id}")
    public ResponseEntity<PaymentDetailDTO> updatePaymentDetail(
            @PathVariable Integer id,
            @RequestBody UpdatePaymentDetailRequestDTO updateRequest) {

        PaymentDetailDTO updated = paymentPeriodService.updatePaymentDetail(id, updateRequest);
        if (updated == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updated);
    }
}
