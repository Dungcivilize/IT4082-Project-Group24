package KTPM.Backend.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import KTPM.Backend.dto.ResidentRequest;
import KTPM.Backend.dto.ResidentResponse;
import KTPM.Backend.entity.Apartment;
import KTPM.Backend.entity.Resident;
import KTPM.Backend.entity.User;
import KTPM.Backend.repository.ResidentRepository;
import KTPM.Backend.repository.UserRepository;

@Service
public class ResidentService {
    @Autowired
    private ResidentRepository residentRepository;

    @Autowired
    private UserRepository userRepository;

    public List<ResidentResponse> getResidentsByUser(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));

        if (user.getApartment() == null) {
            throw new RuntimeException("Người dùng chưa được gán căn hộ");
        }

        List<Resident> residents = residentRepository.findByApartment(user.getApartment());
        return residents.stream()
                .map(resident -> new ResidentResponse(
                    resident.getResidentId(),
                    resident.getFullName(),
                    resident.getBirthDate(),
                    resident.getGender(),
                    resident.getIdentityCard(),
                    resident.getPhone(),
                    resident.getEmail(),
                    resident.getOccupation(),
                    resident.getResidentType(),
                    resident.getRelationship(),
                    resident.getStatus()
                ))
                .collect(Collectors.toList());
    }

    public ResidentResponse addResident(Integer userId, ResidentRequest request) {
        if (request.getFullName() == null || request.getBirthDate() == null || 
            request.getGender() == null || request.getIdentityCard() == null ||
            request.getResidentType() == null) {
            throw new RuntimeException("Vui lòng điền đầy đủ thông tin bắt buộc");
        }

        // Kiểm tra nếu là thành viên thì phải có quan hệ với chủ hộ
        if (request.getResidentType() == Resident.ResidentType.member && 
            (request.getRelationship() == null || request.getRelationship().trim().isEmpty())) {
            throw new RuntimeException("Vui lòng nhập quan hệ với chủ hộ");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));

        Apartment apartment = user.getApartment();
        if (apartment == null) {
            throw new RuntimeException("Người dùng chưa được gán căn hộ");
        }

        // Kiểm tra nếu thêm chủ hộ mới thì căn hộ không được có chủ hộ khác
        if (request.getResidentType() == Resident.ResidentType.owner) {
            boolean hasOwner = residentRepository.findByApartment(apartment).stream()
                    .anyMatch(r -> r.getResidentType() == Resident.ResidentType.owner);
            if (hasOwner) {
                throw new RuntimeException("Căn hộ đã có chủ hộ");
            }
        }

        Resident resident = new Resident();
        resident.setApartment(apartment);
        resident.setFullName(request.getFullName());
        resident.setBirthDate(request.getBirthDate());
        resident.setGender(request.getGender());
        resident.setIdentityCard(request.getIdentityCard());
        resident.setPhone(request.getPhone());
        resident.setEmail(request.getEmail());
        resident.setOccupation(request.getOccupation());
        resident.setResidentType(request.getResidentType());
        
        // Chỉ set relationship nếu là thành viên
        if (request.getResidentType() == Resident.ResidentType.member) {
            resident.setRelationship(request.getRelationship());
        } else {
            resident.setRelationship(null); // Chủ hộ không có quan hệ
        }
        
        resident.setStatus(request.getStatus() != null ? request.getStatus() : Resident.ResidentStatus.living);
        resident = residentRepository.save(resident);

        return new ResidentResponse(
            resident.getResidentId(),
            resident.getFullName(),
            resident.getBirthDate(),
            resident.getGender(),
            resident.getIdentityCard(),
            resident.getPhone(),
            resident.getEmail(),
            resident.getOccupation(),
            resident.getResidentType(),
            resident.getRelationship(),
            resident.getStatus()
        );
    }

    public ResidentResponse updateResident(Integer userId, Integer residentId, ResidentRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));

        if (user.getApartment() == null) {
            throw new RuntimeException("Người dùng chưa được gán căn hộ");
        }

        Resident resident = residentRepository.findById(residentId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy thành viên"));

        // Kiểm tra xem resident có thuộc căn hộ của user không
        if (!resident.getApartment().getApartmentId().equals(user.getApartment().getApartmentId())) {
            throw new RuntimeException("Không có quyền chỉnh sửa thông tin thành viên này");
        }

        // Nếu đang thay đổi loại cư dân
        if (request.getResidentType() != null && request.getResidentType() != resident.getResidentType()) {
            // Nếu đổi từ thành viên sang chủ hộ
            if (request.getResidentType() == Resident.ResidentType.owner) {
                // Kiểm tra xem căn hộ đã có chủ hộ khác chưa
                boolean hasOtherOwner = residentRepository.findByApartment(user.getApartment()).stream()
                        .anyMatch(r -> r.getResidentType() == Resident.ResidentType.owner && !r.getResidentId().equals(residentId));
                if (hasOtherOwner) {
                    throw new RuntimeException("Căn hộ đã có chủ hộ");
                }
                resident.setRelationship(null); // Xóa quan hệ khi chuyển thành chủ hộ
            }
            // Nếu đổi từ chủ hộ sang thành viên
            else if (request.getResidentType() == Resident.ResidentType.member) {
                if (request.getRelationship() == null || request.getRelationship().trim().isEmpty()) {
                    throw new RuntimeException("Vui lòng nhập quan hệ với chủ hộ");
                }
                resident.setRelationship(request.getRelationship());
            }
            resident.setResidentType(request.getResidentType());
        }

        // Cập nhật các trường khác nếu được gửi lên
        if (request.getFullName() != null) {
            resident.setFullName(request.getFullName());
        }
        if (request.getBirthDate() != null) {
            resident.setBirthDate(request.getBirthDate());
        }
        if (request.getGender() != null) {
            resident.setGender(request.getGender());
        }
        if (request.getIdentityCard() != null) {
            resident.setIdentityCard(request.getIdentityCard());
        }
        if (request.getPhone() != null) {
            resident.setPhone(request.getPhone());
        }
        if (request.getEmail() != null) {
            resident.setEmail(request.getEmail());
        }
        if (request.getOccupation() != null) {
            resident.setOccupation(request.getOccupation());
        }
        if (request.getStatus() != null) {
            resident.setStatus(request.getStatus());
        }
        // Chỉ cập nhật relationship nếu là thành viên và có gửi relationship mới
        if (resident.getResidentType() == Resident.ResidentType.member && request.getRelationship() != null) {
            resident.setRelationship(request.getRelationship());
        }

        resident = residentRepository.save(resident);
        return new ResidentResponse(
            resident.getResidentId(),
            resident.getFullName(),
            resident.getBirthDate(),
            resident.getGender(),
            resident.getIdentityCard(),
            resident.getPhone(),
            resident.getEmail(),
            resident.getOccupation(),
            resident.getResidentType(),
            resident.getRelationship(),
            resident.getStatus()
        );
    }

    public void deleteResident(Integer userId, Integer residentId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));

        if (user.getApartment() == null) {
            throw new RuntimeException("Người dùng chưa được gán căn hộ");
        }

        Resident resident = residentRepository.findById(residentId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy thành viên"));

        // Kiểm tra xem resident có thuộc căn hộ của user không
        if (!resident.getApartment().getApartmentId().equals(user.getApartment().getApartmentId())) {
            throw new RuntimeException("Không có quyền xóa thành viên này");
        }

        // Không cho phép xóa chủ hộ nếu còn thành viên khác
        if (resident.getResidentType() == Resident.ResidentType.owner) {
            long memberCount = residentRepository.findByApartment(user.getApartment()).stream()
                    .filter(r -> !r.getResidentId().equals(residentId))
                    .count();
            if (memberCount > 0) {
                throw new RuntimeException("Không thể xóa chủ hộ khi còn thành viên khác trong hộ");
            }
        }

        residentRepository.delete(resident);
    }
} 