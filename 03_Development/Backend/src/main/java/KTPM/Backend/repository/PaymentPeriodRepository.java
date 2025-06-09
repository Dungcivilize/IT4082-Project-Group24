package KTPM.Backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import KTPM.Backend.entity.PaymentPeriod;

@Repository
public interface PaymentPeriodRepository extends JpaRepository<PaymentPeriod, Integer> {
    boolean existsByMonthAndYear(Integer month, Integer year);
} 