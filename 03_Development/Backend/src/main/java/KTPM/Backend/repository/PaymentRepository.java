package KTPM.Backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import KTPM.Backend.entity.Payment;
import KTPM.Backend.entity.PaymentDetail;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Integer> {
    Optional<Payment> findByPaymentDetail(PaymentDetail paymentDetail);
    boolean existsByPaymentDetail(PaymentDetail paymentDetail);

    List<Payment> findByStatus(Payment.PaymentStatus status);
} 