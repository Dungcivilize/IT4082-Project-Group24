package KTPM.Backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import KTPM.Backend.Admin.dto.AdminApartmentDTO;
import KTPM.Backend.entity.Apartment;

import java.util.List;
@Repository
public interface ApartmentRepository extends JpaRepository<Apartment, Integer> {
    @Query("SELECT new KTPM.Backend.Admin.dto.AdminApartmentDTO(a.id, a.apartmentCode, a.floor) " +
           "FROM Apartment a WHERE a.status = 'empty'")
    List<AdminApartmentDTO> findEmptyApartments();
} 