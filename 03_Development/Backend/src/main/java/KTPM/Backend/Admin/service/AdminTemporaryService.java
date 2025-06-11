package KTPM.Backend.Admin.service;

import KTPM.Backend.Admin.dto.AdminTemporaryAbsentDTO;
import KTPM.Backend.Admin.dto.AdminTemporaryResidentDTO;
import KTPM.Backend.entity.TemporaryAbsent;
import KTPM.Backend.entity.TemporaryResident;
import KTPM.Backend.repository.TemporaryAbsentRepository;
import KTPM.Backend.repository.TemporaryResidentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminTemporaryService {

    private final TemporaryResidentRepository temporaryResidentRepository;
    private final TemporaryAbsentRepository temporaryAbsentRepository;

    public List<AdminTemporaryResidentDTO> getTemporaryResidentsByOwnershipId(Integer ownershipId) {
        List<TemporaryResident> residents = temporaryResidentRepository.findByOwnership_OwnershipId(ownershipId);
        return residents.stream().map(r -> new AdminTemporaryResidentDTO(
                r.getTemporaryResidentId(),
                r.getFullName(),
                r.getBirthDate(),
                r.getGender(),
                r.getIdentityCard(),
                r.getPhone(),
                r.getStartDate(),
                r.getEndDate(),
                r.getReason()
        )).collect(Collectors.toList());
    }

    public List<AdminTemporaryAbsentDTO> getTemporaryAbsentsByOwnershipId(Integer ownershipId) {
        List<TemporaryAbsent> absents = temporaryAbsentRepository.findByResident_Ownership_OwnershipId(ownershipId);
        return absents.stream().map(a -> new AdminTemporaryAbsentDTO(
                a.getTemporaryAbsentId(),
                a.getResident().getFullName(),
                a.getStartDate(),
                a.getEndDate(),
                a.getTemporaryAddress(),
                a.getReason()
        )).collect(Collectors.toList());
    }
} 
