package KTPM.Backend.Admin.service;

import KTPM.Backend.Admin.dto.AdminResidentDTO;
import KTPM.Backend.repository.ResidentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class AdminResidentService {

    @Autowired
    private ResidentRepository residentRepository;

    public List<AdminResidentDTO> getResidentsByOwnershipId(Integer ownershipId) {
        return residentRepository.findResidentsByOwnershipId(ownershipId);
    }
}
