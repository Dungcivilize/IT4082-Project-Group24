package KTPM.Backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import KTPM.Backend.entity.Apartment;
import KTPM.Backend.entity.TemporaryResident;

@Repository
public interface TemporaryResidentRepository extends JpaRepository<TemporaryResident, Integer> {
    List<TemporaryResident> findByApartment(Apartment apartment);
} 