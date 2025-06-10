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

    // Tìm theo danh sách ownership
    @Query("SELECT pd FROM PaymentDetail pd WHERE pd.ownership IN :ownerships AND pd.paymentPeriod.paymentPeriodId = :paymentPeriodId")
    List<PaymentDetail> findByOwnershipInAndPaymentPeriodPaymentPeriodId(
            @Param("ownerships") List<ApartmentOwnership> ownerships,
            @Param("paymentPeriodId") Integer paymentPeriodId);

    @Query("SELECT pd FROM PaymentDetail pd WHERE pd.ownership IN :ownerships")
    List<PaymentDetail> findByOwnershipIn(@Param("ownerships") List<ApartmentOwnership> ownerships);

    @Query("SELECT pd FROM PaymentDetail pd WHERE pd.status = :status AND pd.ownership IN :ownerships AND pd.paymentPeriod.paymentPeriodId = :paymentPeriodId")
    List<PaymentDetail> findByStatusAndOwnershipInAndPaymentPeriodPaymentPeriodId(
            @Param("status") Status status,
            @Param("ownerships") List<ApartmentOwnership> ownerships,
            @Param("paymentPeriodId") Integer paymentPeriodId);

    @Query("SELECT pd FROM PaymentDetail pd WHERE pd.status = :status AND pd.ownership IN :ownerships")
    List<PaymentDetail> findByStatusAndOwnershipIn(
            @Param("status") Status status,
            @Param("ownerships") List<ApartmentOwnership> ownerships);

    // Tìm theo trạng thái
    List<PaymentDetail> findByStatus(Status status);

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

    @Query("SELECT pd FROM PaymentDetail pd WHERE pd.ownership.ownershipId = :ownershipId")
    List<PaymentDetail> findByOwnershipId(@Param("ownershipId") Integer ownershipId);

    @Query("SELECT pd FROM PaymentDetail pd WHERE pd.ownership IN :ownerships AND pd.status IN :statuses")
    List<PaymentDetail> findByOwnershipInAndStatusIn(
            @Param("ownerships") List<ApartmentOwnership> ownerships,
            @Param("statuses") List<Status> statuses);

    @Query("SELECT pd FROM PaymentDetail pd WHERE pd.ownership.apartment.apartmentCode = :apartmentCode")
    List<PaymentDetail> findByApartmentCode(@Param("apartmentCode") String apartmentCode);

    @Query("SELECT pd FROM PaymentDetail pd WHERE pd.paymentPeriod.paymentPeriodId = :paymentPeriodId")
    List<PaymentDetail> findByPaymentPeriodId(@Param("paymentPeriodId") Integer paymentPeriodId);

    @Query("SELECT pd FROM PaymentDetail pd WHERE pd.paymentPeriod.paymentPeriodId = :paymentPeriodId " +
           "AND pd.ownership.apartment.apartmentCode = :apartmentCode")
    List<PaymentDetail> findByPaymentPeriodIdAndApartmentCode(
            @Param("paymentPeriodId") Integer paymentPeriodId,
            @Param("apartmentCode") String apartmentCode);

    @Query("SELECT pd FROM PaymentDetail pd WHERE pd.status = :status " +
           "AND pd.paymentPeriod.paymentPeriodId = :paymentPeriodId")
    List<PaymentDetail> findByStatusAndPaymentPeriodId(
            @Param("status") Status status,
            @Param("paymentPeriodId") Integer paymentPeriodId);

    @Query("SELECT pd FROM PaymentDetail pd WHERE pd.status = :status " +
           "AND pd.ownership.apartment.apartmentCode = :apartmentCode")
    List<PaymentDetail> findByStatusAndApartmentCode(
            @Param("status") Status status,
            @Param("apartmentCode") String apartmentCode);

    @Query("SELECT pd FROM PaymentDetail pd WHERE pd.status = :status " +
           "AND pd.paymentPeriod.paymentPeriodId = :paymentPeriodId " +
           "AND pd.ownership.apartment.apartmentCode = :apartmentCode")
    List<PaymentDetail> findByStatusAndPaymentPeriodIdAndApartmentCode(
            @Param("status") Status status,
            @Param("paymentPeriodId") Integer paymentPeriodId,
            @Param("apartmentCode") String apartmentCode);

    @Query("SELECT pd FROM PaymentDetail pd WHERE pd.status = :status")
    List<PaymentDetail> findAllByStatus(@Param("status") Status status);
} 