package KTPM.Backend.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import KTPM.Backend.dto.ResidentTemporaryResidentRequest;
import KTPM.Backend.dto.ResidentTemporaryResidentResponse;
import KTPM.Backend.entity.ApartmentOwnership;
import KTPM.Backend.entity.TemporaryResident;
import KTPM.Backend.entity.User;
import KTPM.Backend.repository.TemporaryResidentRepository;
import KTPM.Backend.repository.UserRepository;
import KTPM.Backend.service.ApartmentOwnershipService;

@Service
public class ResidentTemporaryResidentService {
    @Autowired
    private TemporaryResidentRepository temporaryResidentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ApartmentOwnershipService apartmentOwnershipService;

    public List<ResidentTemporaryResidentResponse> getTemporaryResidents(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));

        ApartmentOwnership ownership = apartmentOwnershipService.getCurrentOwnershipByUserId(userId);

        List<TemporaryResident> temporaryResidents = temporaryResidentRepository.findByOwnership(ownership);
        return temporaryResidents.stream()
                .map(tr -> new ResidentTemporaryResidentResponse(
                    tr.getTemporaryResidentId(),
                    tr.getFullName(),
                    tr.getBirthDate(),
                    tr.getGender(),
                    tr.getIdentityCard(),
                    tr.getPhone(),
                    tr.getStartDate(),
                    tr.getEndDate(),
                    tr.getReason(),
                    tr.getOwnership().getApartment().getApartmentCode()
                ))
                .collect(Collectors.toList());
    }

    public ResidentTemporaryResidentResponse registerTemporaryResident(Integer userId, ResidentTemporaryResidentRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));

        ApartmentOwnership ownership = apartmentOwnershipService.getCurrentOwnershipByUserId(userId);

        // Validate dates
        if (request.getStartDate().isAfter(request.getEndDate())) {
            throw new RuntimeException("Ngày bắt đầu phải trước ngày kết thúc");
        }

        TemporaryResident temporaryResident = new TemporaryResident();
        temporaryResident.setOwnership(ownership);
        temporaryResident.setFullName(request.getFullName());
        temporaryResident.setBirthDate(request.getBirthDate());
        temporaryResident.setGender(request.getGender());
        temporaryResident.setIdentityCard(request.getIdentityCard());
        temporaryResident.setPhone(request.getPhone());
        temporaryResident.setStartDate(request.getStartDate());
        temporaryResident.setEndDate(request.getEndDate());
        temporaryResident.setReason(request.getReason());

        temporaryResident = temporaryResidentRepository.save(temporaryResident);

        return new ResidentTemporaryResidentResponse(
            temporaryResident.getTemporaryResidentId(),
            temporaryResident.getFullName(),
            temporaryResident.getBirthDate(),
            temporaryResident.getGender(),
            temporaryResident.getIdentityCard(),
            temporaryResident.getPhone(),
            temporaryResident.getStartDate(),
            temporaryResident.getEndDate(),
            temporaryResident.getReason(),
            temporaryResident.getOwnership().getApartment().getApartmentCode()
        );
    }

    public void deleteTemporaryResident(Integer userId, Integer temporaryResidentId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));

        ApartmentOwnership ownership = apartmentOwnershipService.getCurrentOwnershipByUserId(userId);

        TemporaryResident temporaryResident = temporaryResidentRepository.findById(temporaryResidentId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người tạm trú"));

        // Kiểm tra xem người tạm trú có thuộc quyền sở hữu của user không
        if (!temporaryResident.getOwnership().getOwnershipId().equals(ownership.getOwnershipId())) {
            throw new RuntimeException("Không có quyền xóa thông tin tạm trú này");
        }

        temporaryResidentRepository.delete(temporaryResident);
    }

    public ResidentTemporaryResidentResponse updateTemporaryResident(Integer userId, Integer temporaryResidentId, ResidentTemporaryResidentRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));

        ApartmentOwnership ownership = apartmentOwnershipService.getCurrentOwnershipByUserId(userId);

        TemporaryResident temporaryResident = temporaryResidentRepository.findById(temporaryResidentId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy thông tin tạm trú"));

        // Kiểm tra xem temporary resident có thuộc quyền sở hữu của user không
        if (!temporaryResident.getOwnership().getOwnershipId().equals(ownership.getOwnershipId())) {
            throw new RuntimeException("Không có quyền cập nhật thông tin tạm trú này");
        }

        // Validate dates
        if (request.getStartDate().isAfter(request.getEndDate())) {
            throw new RuntimeException("Ngày bắt đầu phải trước ngày kết thúc");
        }

        temporaryResident.setFullName(request.getFullName());
        temporaryResident.setBirthDate(request.getBirthDate());
        temporaryResident.setGender(request.getGender());
        temporaryResident.setIdentityCard(request.getIdentityCard());
        temporaryResident.setPhone(request.getPhone());
        temporaryResident.setStartDate(request.getStartDate());
        temporaryResident.setEndDate(request.getEndDate());
        temporaryResident.setReason(request.getReason());

        temporaryResident = temporaryResidentRepository.save(temporaryResident);

        return new ResidentTemporaryResidentResponse(
            temporaryResident.getTemporaryResidentId(),
            temporaryResident.getFullName(),
            temporaryResident.getBirthDate(),
            temporaryResident.getGender(),
            temporaryResident.getIdentityCard(),
            temporaryResident.getPhone(),
            temporaryResident.getStartDate(),
            temporaryResident.getEndDate(),
            temporaryResident.getReason(),
            temporaryResident.getOwnership().getApartment().getApartmentCode()
        );
    }
} 