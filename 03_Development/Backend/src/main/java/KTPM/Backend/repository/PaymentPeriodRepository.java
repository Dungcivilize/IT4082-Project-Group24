package KTPM.Backend.repository;

import KTPM.Backend.entity.PaymentPeriod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentPeriodRepository extends JpaRepository<PaymentPeriod, Integer> {

}
