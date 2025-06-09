package KTPM.Backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

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
} 