package KTPM.Backend.Admin.service;

import KTPM.Backend.Admin.dto.AdminOwnershipDTO;
import KTPM.Backend.repository.ApartmentOwnershipRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminOwnershipService {
    private final ApartmentOwnershipRepository ownershipRepository;

    public List<AdminOwnershipDTO> getActiveOwnerships() {
        return ownershipRepository.findActiveOwnershipDTOs();
    }
}
