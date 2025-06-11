package KTPM.Backend.Admin.service;

import KTPM.Backend.Admin.dto.AdminOwnershipDTO;
import KTPM.Backend.entity.ApartmentOwnership;
import KTPM.Backend.repository.ApartmentOwnershipRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
 import java.time.LocalDate;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminOwnershipService {
    private final ApartmentOwnershipRepository ownershipRepository;

    public List<AdminOwnershipDTO> getActiveOwnerships() {
        return ownershipRepository.findActiveOwnershipDTOs();
    }

   

public AdminOwnershipDTO updateOwnershipStatus(Integer id, AdminOwnershipDTO updatedOwnership) {
    ApartmentOwnership ownership = ownershipRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Ownership not found"));

    // Cập nhật status
    if (updatedOwnership.getOwnershipStatus() != null) {
        ownership.setStatus(updatedOwnership.getOwnershipStatus());

        // Nếu chuyển thành inactive → set endDate
        if (updatedOwnership.getOwnershipStatus() == ApartmentOwnership.Status.inactive) {
            ownership.setEndDate(LocalDate.now());
        }
    }

    ApartmentOwnership saved = ownershipRepository.save(ownership);

    return new AdminOwnershipDTO(
            saved.getOwnershipId(),
            saved.getUser().getFullName(),
            saved.getApartment().getApartmentId(),
            saved.getApartment().getApartmentCode(),
            saved.getApartment().getFloor(),
            saved.getStatus()
    );
}


}
