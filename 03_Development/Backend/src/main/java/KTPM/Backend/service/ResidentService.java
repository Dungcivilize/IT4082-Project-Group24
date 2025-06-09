package KTPM.Backend.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import KTPM.Backend.entity.ApartmentOwnership;
import KTPM.Backend.entity.Resident;
import KTPM.Backend.dto.ResidentRequest;
import KTPM.Backend.dto.ResidentResponse;
import KTPM.Backend.repository.ResidentRepository;

@Service
public class ResidentService {
    @Autowired
    private ResidentRepository residentRepository;

    @Autowired
    private ApartmentOwnershipService apartmentOwnershipService;

    public List<ResidentResponse> getAllResidents() {
        return residentRepository.findAll().stream()
                .map(ResidentResponse::fromEntity)
                .collect(Collectors.toList());
    }

    public ResidentResponse getResidentById(Integer id) {
        Resident resident = residentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy cư dân"));
        return ResidentResponse.fromEntity(resident);
    }

    public List<ResidentResponse> getResidentsByOwnership(Integer ownershipId) {
        return residentRepository.findByOwnershipOwnershipId(ownershipId).stream()
                .map(ResidentResponse::fromEntity)
                .collect(Collectors.toList());
    }

    public List<ResidentResponse> getResidentsByUser(Integer userId) {
        // Lấy ownership hiện tại của user
        ApartmentOwnership ownership = apartmentOwnershipService.getCurrentOwnershipByUserId(userId);
        
        // Lấy danh sách cư dân theo ownership
        return residentRepository.findByOwnershipOwnershipId(ownership.getOwnershipId()).stream()
                .map(ResidentResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public ResidentResponse addResident(Integer userId, ResidentRequest request) {
        // Lấy ownership hiện tại của user
        ApartmentOwnership ownership = apartmentOwnershipService.getCurrentOwnershipByUserId(userId);

        // Kiểm tra CMND/CCCD đã tồn tại chưa
        if (request.getIdentityCard() != null) {
            residentRepository.findByIdentityCard(request.getIdentityCard())
                    .ifPresent(r -> {
                        throw new RuntimeException("CMND/CCCD đã tồn tại");
                    });
        }

        // Chuyển đổi từ request sang entity
        Resident resident = request.toEntity();
        resident.setOwnership(ownership);

        // Lưu vào database
        Resident savedResident = residentRepository.save(resident);
        return ResidentResponse.fromEntity(savedResident);
    }

    @Transactional
    public ResidentResponse updateResident(Integer userId, Integer residentId, ResidentRequest request) {
        // Lấy ownership hiện tại của user
        ApartmentOwnership ownership = apartmentOwnershipService.getCurrentOwnershipByUserId(userId);

        // Lấy thông tin cư dân
        Resident resident = residentRepository.findById(residentId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy cư dân"));

        // Kiểm tra quyền sở hữu
        if (!ownership.getOwnershipId().equals(resident.getOwnership().getOwnershipId())) {
            throw new RuntimeException("Không có quyền cập nhật thông tin cư dân này");
        }

        // Kiểm tra CMND/CCCD đã tồn tại chưa (nếu có thay đổi)
        if (request.getIdentityCard() != null &&
                !request.getIdentityCard().equals(resident.getIdentityCard())) {
            residentRepository.findByIdentityCard(request.getIdentityCard())
                    .ifPresent(r -> {
                        throw new RuntimeException("CMND/CCCD đã tồn tại");
                    });
        }

        // Cập nhật thông tin từ request
        resident.setFullName(request.getFullName());
        resident.setBirthDate(request.getBirthDate());
        resident.setGender(Resident.Gender.valueOf(request.getGender().toLowerCase()));
        resident.setIdentityCard(request.getIdentityCard());
        resident.setPhone(request.getPhone());
        resident.setEmail(request.getEmail());
        resident.setOccupation(request.getOccupation());
        resident.setResidentType(Resident.ResidentType.valueOf(request.getResidentType().toLowerCase()));
        resident.setRelationship(request.getRelationship());

        Resident savedResident = residentRepository.save(resident);
        return ResidentResponse.fromEntity(savedResident);
    }

    @Transactional
    public void deleteResident(Integer userId, Integer residentId) {
        // Lấy ownership hiện tại của user
        ApartmentOwnership ownership = apartmentOwnershipService.getCurrentOwnershipByUserId(userId);

        // Lấy thông tin cư dân
        Resident resident = residentRepository.findById(residentId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy cư dân"));

        // Kiểm tra quyền sở hữu
        if (!resident.getOwnership().getOwnershipId().equals(ownership.getOwnershipId())) {
            throw new RuntimeException("Không có quyền xóa cư dân này");
        }

        residentRepository.delete(resident);
    }
} 