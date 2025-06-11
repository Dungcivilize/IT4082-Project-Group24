package KTPM.Backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import KTPM.Backend.entity.Apartment;

import java.util.List;
@Repository
public interface ApartmentRepository extends JpaRepository<Apartment, Integer> {
    boolean existsByApartmentCode(String apartmentCode);

    List<Apartment> findByStatus(Apartment.Status status);
} 