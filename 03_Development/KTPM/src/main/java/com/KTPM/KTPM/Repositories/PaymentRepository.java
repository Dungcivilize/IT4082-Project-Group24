package com.KTPM.KTPM.Repositories;

import com.KTPM.KTPM.Models.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findByCitizen_CitizenId(Long citizenId);
    List<Payment> findByCitizen_CitizenIdAndStatus(Long citizenId, Payment.PaymentStatus status);
}
