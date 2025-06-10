package KTPM.Backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import KTPM.Backend.Admin.dto.AdminResidentDTO;
import KTPM.Backend.entity.ApartmentOwnership;
import KTPM.Backend.entity.Resident;

@Repository
public interface ResidentRepository extends JpaRepository<Resident, Integer> {
    List<Resident> findByOwnershipOwnershipId(Integer ownershipId);
    Optional<Resident> findByIdentityCard(String identityCard);

    List<Resident> findByOwnershipAndResidentType(ApartmentOwnership ownership, Resident.ResidentType residentType);

    @Query("SELECT new KTPM.Backend.Admin.dto.AdminResidentDTO(" +
       "r.residentId, r.ownership.ownershipId, r.fullName, r.birthDate, " +
       "r.gender, r.identityCard, r.phone, r.email, r.occupation, " +
       "r.residentType, r.relationship, r.status) " +
       "FROM Resident r " +
       "WHERE r.ownership.ownershipId = :ownershipId")
    List<AdminResidentDTO> findResidentsByOwnershipId(@Param("ownershipId") Integer ownershipId);

} 