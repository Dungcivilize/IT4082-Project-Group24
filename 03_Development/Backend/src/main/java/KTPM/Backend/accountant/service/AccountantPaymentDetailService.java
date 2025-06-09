package KTPM.Backend.accountant.service;

import KTPM.Backend.entity.PaymentDetail;
import KTPM.Backend.repository.PaymentDetailRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class AccountantPaymentDetailService {

    private final PaymentDetailRepository paymentDetailRepository;

    public AccountantPaymentDetailService(PaymentDetailRepository paymentDetailRepository) {
        this.paymentDetailRepository = paymentDetailRepository;
    }

    @Transactional
    public void updateAmount(Integer paymentDetailId, BigDecimal newAmount) {
        PaymentDetail pd = paymentDetailRepository.findById(paymentDetailId)
                .orElseThrow(() -> new RuntimeException("PaymentDetail not found"));
        pd.setAmount(newAmount);
        paymentDetailRepository.save(pd);
    }
}
