package KTPM.Backend.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import KTPM.Backend.entity.Apartment;
import KTPM.Backend.entity.Resident;
import KTPM.Backend.entity.TemporaryAbsent;

@Repository
public interface TemporaryAbsentRepository extends JpaRepository<TemporaryAbsent, Integer> {
    List<TemporaryAbsent> findByResident_Apartment(Apartment apartment);

    @Query("SELECT CASE WHEN COUNT(ta) > 0 THEN true ELSE false END FROM TemporaryAbsent ta " +
           "WHERE ta.resident = :resident " +
           "AND ((ta.startDate BETWEEN :startDate AND :endDate) " +
           "OR (ta.endDate BETWEEN :startDate AND :endDate) " +
           "OR (:startDate BETWEEN ta.startDate AND ta.endDate))")
    boolean existsByResidentAndDateRange(
            @Param("resident") Resident resident,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);
} 