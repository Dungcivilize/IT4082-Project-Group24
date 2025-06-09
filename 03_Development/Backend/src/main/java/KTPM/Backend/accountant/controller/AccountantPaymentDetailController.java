package KTPM.Backend.accountant.controller;

import KTPM.Backend.accountant.service.AccountantPaymentDetailService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/api/accountant")
public class AccountantPaymentDetailController {

    private final AccountantPaymentDetailService paymentDetailService;

    public AccountantPaymentDetailController(AccountantPaymentDetailService paymentDetailService) {
        this.paymentDetailService = paymentDetailService;
    }

    @PatchMapping("/payment-details/{id}")
    public ResponseEntity<?> updatePaymentDetailAmount(@PathVariable Integer id, @RequestBody Map<String, Object> payload) {
        try {
            BigDecimal amount = new BigDecimal(payload.get("amount").toString());
            paymentDetailService.updateAmount(id, amount);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Invalid request");
        }
    }
}
