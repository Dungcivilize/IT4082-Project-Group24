package KTPM.Backend.repository;


import KTPM.Backend.entity.Apartment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApartmentRepository extends JpaRepository<Apartment, Long> {
    List<Apartment> findByStatus(Apartment.ApartmentStatus status);// ví dụ: "occupied"
}
