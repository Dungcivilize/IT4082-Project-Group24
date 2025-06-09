package KTPM.Backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import KTPM.Backend.entity.ApartmentOwnership;
import KTPM.Backend.entity.Vehicle;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Integer> {
    List<Vehicle> findByOwnership(ApartmentOwnership ownership);
    boolean existsByLicensePlate(String licensePlate);
} 