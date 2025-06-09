package KTPM.Backend.accountant.service;

import KTPM.Backend.entity.Payment;
import KTPM.Backend.entity.PaymentDetail;
import KTPM.Backend.repository.PaymentDetailRepository;
import KTPM.Backend.repository.PaymentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AccountantPaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentDetailRepository paymentDetailRepository;

    // Chỉ giữ 1 constructor duy nhất có đủ tham số để Spring autowire
    public AccountantPaymentService(PaymentRepository paymentRepository,
                                    PaymentDetailRepository paymentDetailRepository) {
        this.paymentRepository = paymentRepository;
        this.paymentDetailRepository = paymentDetailRepository;
    }

    public List<Payment> getProcessingPayments() {
        return paymentRepository.findByStatus(Payment.PaymentStatus.PROCESSING);
    }

    @Transactional
    public void approvePayment(Integer paymentId) throws Exception {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new Exception("Payment không tồn tại"));

        if (payment.getStatus() != Payment.PaymentStatus.PROCESSING) {
            throw new Exception("Payment không ở trạng thái PROCESSING");
        }

        payment.setStatus(Payment.PaymentStatus.PAID);
        paymentRepository.save(payment);

        PaymentDetail detail = payment.getPaymentDetail();
        if (detail != null) {
            detail.setStatus(PaymentDetail.PaymentDetailStatus.paid);
            paymentDetailRepository.save(detail);
        }
    }

    @Transactional
    public void rejectPayment(Integer paymentId, String rejectNote) throws Exception {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new Exception("Payment không tồn tại"));

        if (payment.getStatus() != Payment.PaymentStatus.PROCESSING) {
            throw new Exception("Payment không ở trạng thái PROCESSING");
        }

        payment.setStatus(Payment.PaymentStatus.UNPAID);
        payment.setNote(rejectNote);
        paymentRepository.save(payment);

        // PaymentDetail giữ nguyên trạng thái (PENDING)
    }
}

