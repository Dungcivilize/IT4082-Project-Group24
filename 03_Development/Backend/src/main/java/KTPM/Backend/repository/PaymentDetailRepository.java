package KTPM.Backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import KTPM.Backend.entity.ApartmentOwnership;
import KTPM.Backend.entity.PaymentDetail;
import KTPM.Backend.entity.PaymentPeriod;
import KTPM.Backend.entity.ServiceType;

@Repository
public interface PaymentDetailRepository extends JpaRepository<PaymentDetail, Integer> {
    List<PaymentDetail> findByPaymentPeriodPaymentPeriodId(Integer paymentPeriodId);

    List<PaymentDetail> findByOwnershipOwnershipId(Integer ownershipId);

    Optional<PaymentDetail> findByPaymentPeriodAndOwnershipAndServiceType(
            PaymentPeriod paymentPeriod,
            ApartmentOwnership ownership,
            ServiceType serviceType);

    List<PaymentDetail> findByOwnershipAndStatus(ApartmentOwnership ownership, PaymentDetail.Status status);
} 