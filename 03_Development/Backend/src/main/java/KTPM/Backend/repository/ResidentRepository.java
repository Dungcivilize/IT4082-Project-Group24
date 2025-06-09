package KTPM.Backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import KTPM.Backend.entity.ApartmentOwnership;
import KTPM.Backend.entity.Resident;

@Repository
public interface ResidentRepository extends JpaRepository<Resident, Integer> {
    List<Resident> findByOwnershipOwnershipId(Integer ownershipId);

    Optional<Resident> findByIdentityCard(String identityCard);

    List<Resident> findByOwnershipAndResidentType(ApartmentOwnership ownership, Resident.ResidentType residentType);
} 