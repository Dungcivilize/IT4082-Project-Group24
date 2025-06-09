package KTPM.Backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import KTPM.Backend.entity.PaymentDetail;
import KTPM.Backend.entity.PaymentDetail.Status;
import KTPM.Backend.entity.ApartmentOwnership;
import KTPM.Backend.entity.PaymentPeriod;
import KTPM.Backend.entity.ServiceType;

@Repository
public interface PaymentDetailRepository extends JpaRepository<PaymentDetail, Integer> {
    // Tìm theo kỳ thu phí
    List<PaymentDetail> findByPaymentPeriodPaymentPeriodId(Integer paymentPeriodId);
    
    // Tìm theo căn hộ
    List<PaymentDetail> findByOwnershipOwnershipId(Integer ownershipId);
    
    // Tìm theo trạng thái
    List<PaymentDetail> findByStatus(Status status);
    
    // Tìm theo trạng thái và kỳ thu phí
    List<PaymentDetail> findByStatusAndPaymentPeriodPaymentPeriodId(Status status, Integer paymentPeriodId);
    
    // Tìm theo trạng thái và căn hộ
    List<PaymentDetail> findByStatusAndOwnershipOwnershipId(Status status, Integer ownershipId);
    
    // Tìm theo kỳ thu phí và căn hộ
    List<PaymentDetail> findByPaymentPeriodPaymentPeriodIdAndOwnershipOwnershipId(Integer paymentPeriodId, Integer ownershipId);
    
    // Tìm theo trạng thái, kỳ thu phí và căn hộ
    List<PaymentDetail> findByStatusAndPaymentPeriodPaymentPeriodIdAndOwnershipOwnershipId(Status status, Integer paymentPeriodId, Integer ownershipId);

    // Tìm theo căn hộ và trạng thái (cho resident)
    List<PaymentDetail> findByOwnershipAndStatus(ApartmentOwnership ownership, Status status);

    // Tìm theo căn hộ và nhiều trạng thái (cho resident)
    List<PaymentDetail> findByOwnershipAndStatusIn(ApartmentOwnership ownership, List<Status> statuses);

    // Kiểm tra tồn tại
    boolean existsByServiceType(ServiceType serviceType);
    
    boolean existsByPaymentPeriodAndOwnershipAndServiceType(PaymentPeriod paymentPeriod, ApartmentOwnership ownership, ServiceType serviceType);
    
    Optional<PaymentDetail> findByPaymentPeriodAndOwnershipAndServiceType(PaymentPeriod paymentPeriod, ApartmentOwnership ownership, ServiceType serviceType);

    @Query("SELECT pd FROM PaymentDetail pd WHERE pd.ownership IN :ownerships AND pd.status = :status")
    List<PaymentDetail> findByOwnershipInAndStatus(
            @Param("ownerships") List<ApartmentOwnership> ownerships,
            @Param("status") Status status);

    @Query("SELECT pd FROM PaymentDetail pd WHERE pd.paymentPeriod.paymentPeriodId = :periodId AND pd.ownership.ownershipId = :ownershipId AND pd.status = :status")
    List<PaymentDetail> findByPaymentPeriodIdAndOwnershipIdAndStatus(
            @Param("periodId") Integer periodId,
            @Param("ownershipId") Integer ownershipId,
            @Param("status") Status status);

    @Query("SELECT pd FROM PaymentDetail pd WHERE pd.paymentPeriod.paymentPeriodId = :periodId AND pd.ownership.ownershipId = :ownershipId")
    List<PaymentDetail> findByPaymentPeriodIdAndOwnershipId(
            @Param("periodId") Integer periodId,
            @Param("ownershipId") Integer ownershipId);

    @Query("SELECT pd FROM PaymentDetail pd WHERE pd.paymentPeriod.paymentPeriodId = :periodId")
    List<PaymentDetail> findByPaymentPeriodId(@Param("periodId") Integer periodId);

    @Query("SELECT pd FROM PaymentDetail pd WHERE pd.ownership.ownershipId = :ownershipId")
    List<PaymentDetail> findByOwnershipId(@Param("ownershipId") Integer ownershipId);

    @Query("SELECT pd FROM PaymentDetail pd WHERE pd.ownership IN :ownerships AND pd.status IN :statuses")
    List<PaymentDetail> findByOwnershipInAndStatusIn(
            @Param("ownerships") List<ApartmentOwnership> ownerships,
            @Param("statuses") List<Status> statuses);
} 