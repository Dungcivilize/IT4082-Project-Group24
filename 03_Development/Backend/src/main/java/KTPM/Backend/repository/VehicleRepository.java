package KTPM.Backend.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import KTPM.Backend.entity.ApartmentOwnership;
import KTPM.Backend.entity.Vehicle;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Integer> {
    List<Vehicle> findByOwnership(ApartmentOwnership ownership);
    boolean existsByLicensePlate(String licensePlate);

    @Query("SELECT COUNT(v) FROM Vehicle v WHERE v.ownership.ownershipId = :ownershipId AND v.type = :type")
    int countActiveVehiclesByOwnershipAndType(@Param("ownershipId") Integer ownershipId, @Param("type") Vehicle.VehicleType type);

    List<Vehicle> findAllByOwnership_OwnershipId(Integer ownershipId);

    @Query("SELECT v.type, COUNT(v) FROM Vehicle v GROUP BY v.type")
List<Object[]> countByTypeRaw();

} 