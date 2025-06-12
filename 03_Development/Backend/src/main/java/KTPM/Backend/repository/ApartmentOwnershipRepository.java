package KTPM.Backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import KTPM.Backend.Admin.dto.AdminOwnershipDTO;
import KTPM.Backend.entity.ApartmentOwnership;

@Repository
public interface ApartmentOwnershipRepository extends JpaRepository<ApartmentOwnership, Integer> {
    @Query("SELECT ao FROM ApartmentOwnership ao " +
           "WHERE ao.apartment.apartmentId = :apartmentId " +
           "AND ao.endDate IS NULL " +
           "AND ao.status = 'active'")
    Optional<ApartmentOwnership> findCurrentOwnershipByApartmentId(@Param("apartmentId") Integer apartmentId);

    @Query("SELECT ao FROM ApartmentOwnership ao " +
           "WHERE ao.user.userId = :userId " +
           "AND ao.endDate IS NULL " +
           "AND ao.status = 'active'")
    Optional<ApartmentOwnership> findCurrentOwnershipByUserId(@Param("userId") Integer userId);

    List<ApartmentOwnership> findByApartmentApartmentId(Integer apartmentId);

    List<ApartmentOwnership> findByUserUserId(Integer userId);
    @Query("SELECT ao FROM ApartmentOwnership ao " +
            "WHERE (ao.apartment.apartmentId, ao.startDate) IN " +
            "(SELECT ao2.apartment.apartmentId, MAX(ao2.startDate) " +
            "FROM ApartmentOwnership ao2 " +
            "GROUP BY ao2.apartment.apartmentId) " +
            "AND ao.status = 'active' " +
            "AND ao.endDate IS NULL " +
            "ORDER BY ao.apartment.apartmentCode")
    List<ApartmentOwnership> findLatestOwnershipForEachApartment();
    @Query("SELECT new KTPM.Backend.Admin.dto.AdminOwnershipDTO(" +
       "ao.ownershipId, " +
       "ao.user.fullName, " +
       "a.apartmentId, " +
       "a.apartmentCode, " +
       "a.floor, " +
       "ao.status) " +
       "FROM ApartmentOwnership ao " +
       "JOIN ao.apartment a " +
       "WHERE ao.status = 'active'")
List<AdminOwnershipDTO> findActiveOwnershipDTOs();

} 