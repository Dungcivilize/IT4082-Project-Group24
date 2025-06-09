package KTPM.Backend.repository;

import KTPM.Backend.entity.Apartment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApartmentRepository extends JpaRepository<Apartment, Integer> {
    // Thêm các method tùy chỉnh nếu cần
}
