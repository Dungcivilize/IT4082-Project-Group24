package com.KTPM.KTPM.Controllers;

import com.KTPM.KTPM.DTO.PaymentDetailDTO;
import com.KTPM.KTPM.Repositories.PaymentRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class PaymentDetailController {

    private final PaymentRepository paymentRepository;

    public PaymentDetailController(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @GetMapping("/payment-details")
    public List<PaymentDetailDTO> getPaymentDetails(@RequestParam String cpName) {
        return paymentRepository.findPaymentsByCpName(cpName);
    }
}
