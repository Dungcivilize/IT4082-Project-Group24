package com.KTPM.KTPM.Repositories;

import com.KTPM.KTPM.DTO.CollectionSummaryByNameDTO;
import com.KTPM.KTPM.DTO.CollectionSummaryDTO;
import com.KTPM.KTPM.DTO.PaymentDetailDTO;
import com.KTPM.KTPM.Models.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findByCitizen_CitizenId(Long citizenId);
    List<Payment> findByCitizen_CitizenIdAndStatus(Long citizenId, Payment.PaymentStatus status);
    List<Payment> findByCollectionPeriod_CpId(Long cpId);

    @Query("""
    SELECT new com.KTPM.KTPM.DTO.CollectionSummaryDTO(
        p.collectionPeriod.cpId,
        p.collectionPeriod.cpName,
        COALESCE(SUM(CASE WHEN p.status = com.KTPM.KTPM.Models.Payment.PaymentStatus.PAID THEN p.cost ELSE 0 END), 0),
        COALESCE(SUM(CASE WHEN p.status = com.KTPM.KTPM.Models.Payment.PaymentStatus.UNPAID THEN p.cost ELSE 0 END), 0),
        COUNT(DISTINCT (CASE WHEN p.status = com.KTPM.KTPM.Models.Payment.PaymentStatus.PAID THEN p.citizen.citizenId ELSE null END)),
        COUNT(DISTINCT (CASE WHEN p.status = com.KTPM.KTPM.Models.Payment.PaymentStatus.UNPAID THEN p.citizen.citizenId ELSE null END))
    )
    FROM Payment p
    GROUP BY p.collectionPeriod.cpId, p.collectionPeriod.cpName
""")

    List<CollectionSummaryDTO> getCollectionSummary();

    @Query(value = """
    SELECT cp.cp_name,
           SUM(CASE WHEN p.status = 'PAID' THEN p.cost ELSE 0 END) AS totalPaid,
           SUM(CASE WHEN p.status = 'UNPAID' THEN p.cost ELSE 0 END) AS totalUnpaid,
           COUNT(DISTINCT CASE WHEN p.status = 'PAID' THEN p.citizen_id ELSE NULL END) AS paidHouseholds,
           COUNT(DISTINCT CASE WHEN p.status = 'UNPAID' THEN p.citizen_id ELSE NULL END) AS unpaidHouseholds
    FROM payment p
    JOIN collection_period cp ON p.cp_id = cp.cp_id
    GROUP BY cp.cp_name
""", nativeQuery = true)
    List<CollectionSummaryByNameDTO> getSummaryByCpName();

    @Query(value = """
    SELECT p.citizen_id, p.status, p.cost, cp.collection_date
    FROM payment p
    JOIN collection_period cp ON p.cp_id = cp.cp_id
    WHERE cp.cp_name = :cpName
""", nativeQuery = true)
    List<PaymentDetailDTO> findPaymentsByCpName(@Param("cpName") String cpName);

}
