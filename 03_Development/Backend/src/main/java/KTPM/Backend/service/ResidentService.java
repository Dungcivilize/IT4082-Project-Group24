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
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));

        Apartment apartment = user.getApartment();
        if (apartment == null) {
            throw new RuntimeException("Người dùng chưa được gán căn hộ");
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
        resident.setRelationship(request.getRelationship());
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

        resident.setFullName(request.getFullName());
        resident.setBirthDate(request.getBirthDate());
        resident.setGender(request.getGender());
        resident.setIdentityCard(request.getIdentityCard());
        resident.setPhone(request.getPhone());
        resident.setEmail(request.getEmail());
        resident.setOccupation(request.getOccupation());
        resident.setResidentType(request.getResidentType());
        resident.setRelationship(request.getRelationship());
        resident.setStatus(request.getStatus() != null ? request.getStatus() : resident.getStatus());

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

        residentRepository.delete(resident);
    }
} 