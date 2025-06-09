package KTPM.Backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import KTPM.Backend.entity.Apartment;
import KTPM.Backend.entity.Vehicle;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Integer> {
    List<Vehicle> findByApartment(Apartment apartment);
    boolean existsByLicensePlate(String licensePlate);

    int countByApartmentAndType(Apartment apartment, Vehicle.VehicleType type);
} 