package KTPM.Backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import KTPM.Backend.entity.Apartment;

@Repository
public interface ApartmentRepository extends JpaRepository<Apartment, Integer> {
} 