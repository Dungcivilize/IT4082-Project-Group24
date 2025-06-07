package KTPM.Backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import KTPM.Backend.entity.Apartment;
import KTPM.Backend.entity.PaymentDetail;

@Repository
public interface PaymentDetailRepository extends JpaRepository<PaymentDetail, Integer> {
    List<PaymentDetail> findByApartment(Apartment apartment);
    List<PaymentDetail> findByApartmentAndStatus(Apartment apartment, PaymentDetail.PaymentDetailStatus status);
} 