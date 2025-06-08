package KTPM.Backend.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import KTPM.Backend.dto.ResidentTemporaryAbsentRequest;
import KTPM.Backend.dto.ResidentTemporaryAbsentResponse;
import KTPM.Backend.entity.Resident;
import KTPM.Backend.entity.TemporaryAbsent;
import KTPM.Backend.entity.User;
import KTPM.Backend.repository.ResidentRepository;
import KTPM.Backend.repository.TemporaryAbsentRepository;
import KTPM.Backend.repository.UserRepository;

@Service
public class ResidentTemporaryAbsentService {
    @Autowired
    private TemporaryAbsentRepository temporaryAbsentRepository;

    @Autowired
    private ResidentRepository residentRepository;

    @Autowired
    private UserRepository userRepository;

    public List<ResidentTemporaryAbsentResponse> getTemporaryAbsents(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));

        if (user.getApartment() == null) {
            throw new RuntimeException("Người dùng chưa được gán căn hộ");
        }

        List<TemporaryAbsent> temporaryAbsents = temporaryAbsentRepository.findByResident_Apartment(user.getApartment());
        return temporaryAbsents.stream()
                .map(ta -> new ResidentTemporaryAbsentResponse(
                    ta.getTemporaryAbsentId(),
                    ta.getResident().getResidentId(),
                    ta.getResident().getFullName(),
                    ta.getResident().getApartment().getApartmentCode(),
                    ta.getStartDate(),
                    ta.getEndDate(),
                    ta.getTemporaryAddress(),
                    ta.getReason()
                ))
                .collect(Collectors.toList());
    }

    public ResidentTemporaryAbsentResponse registerTemporaryAbsent(Integer userId, Integer residentId, ResidentTemporaryAbsentRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));

        if (user.getApartment() == null) {
            throw new RuntimeException("Người dùng chưa được gán căn hộ");
        }

        Resident resident = residentRepository.findById(residentId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy thành viên"));

        // Kiểm tra xem resident có thuộc căn hộ của user không
        if (!resident.getApartment().getApartmentId().equals(user.getApartment().getApartmentId())) {
            throw new RuntimeException("Không có quyền đăng ký tạm vắng cho thành viên này");
        }

        // Validate dates
        if (request.getStartDate().isAfter(request.getEndDate())) {
            throw new RuntimeException("Ngày bắt đầu phải trước ngày kết thúc");
        }

        // Kiểm tra xem có đang tạm vắng trong khoảng thời gian này không
        if (temporaryAbsentRepository.existsByResidentAndDateRange(
                resident, request.getStartDate(), request.getEndDate())) {
            throw new RuntimeException("Thành viên đã đăng ký tạm vắng trong khoảng thời gian này");
        }

        TemporaryAbsent temporaryAbsent = new TemporaryAbsent();
        temporaryAbsent.setResident(resident);
        temporaryAbsent.setStartDate(request.getStartDate());
        temporaryAbsent.setEndDate(request.getEndDate());
        temporaryAbsent.setTemporaryAddress(request.getTemporaryAddress());
        temporaryAbsent.setReason(request.getReason());

        temporaryAbsent = temporaryAbsentRepository.save(temporaryAbsent);

        // Cập nhật trạng thái của resident
        resident.setStatus(Resident.ResidentStatus.temporary_absent);
        residentRepository.save(resident);

        return new ResidentTemporaryAbsentResponse(
            temporaryAbsent.getTemporaryAbsentId(),
            temporaryAbsent.getResident().getResidentId(),
            temporaryAbsent.getResident().getFullName(),
            temporaryAbsent.getResident().getApartment().getApartmentCode(),
            temporaryAbsent.getStartDate(),
            temporaryAbsent.getEndDate(),
            temporaryAbsent.getTemporaryAddress(),
            temporaryAbsent.getReason()
        );
    }

    public void deleteTemporaryAbsent(Integer userId, Integer temporaryAbsentId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));

        if (user.getApartment() == null) {
            throw new RuntimeException("Người dùng chưa được gán căn hộ");
        }

        TemporaryAbsent temporaryAbsent = temporaryAbsentRepository.findById(temporaryAbsentId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy thông tin tạm vắng"));

        // Kiểm tra xem temporary absent có thuộc căn hộ của user không
        if (!temporaryAbsent.getResident().getApartment().getApartmentId().equals(user.getApartment().getApartmentId())) {
            throw new RuntimeException("Không có quyền xóa thông tin tạm vắng này");
        }

        // Cập nhật trạng thái của resident
        Resident resident = temporaryAbsent.getResident();
        resident.setStatus(Resident.ResidentStatus.living);
        residentRepository.save(resident);

        temporaryAbsentRepository.delete(temporaryAbsent);
    }

    public ResidentTemporaryAbsentResponse updateTemporaryAbsent(Integer userId, Integer temporaryAbsentId, ResidentTemporaryAbsentRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));

        if (user.getApartment() == null) {
            throw new RuntimeException("Người dùng chưa được gán căn hộ");
        }

        TemporaryAbsent temporaryAbsent = temporaryAbsentRepository.findById(temporaryAbsentId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy thông tin tạm vắng"));

        // Kiểm tra xem temporary absent có thuộc căn hộ của user không
        if (!temporaryAbsent.getResident().getApartment().getApartmentId().equals(user.getApartment().getApartmentId())) {
            throw new RuntimeException("Không có quyền cập nhật thông tin tạm vắng này");
        }

        // Validate dates
        if (request.getStartDate().isAfter(request.getEndDate())) {
            throw new RuntimeException("Ngày bắt đầu phải trước ngày kết thúc");
        }

        // Kiểm tra xem có đang tạm vắng trong khoảng thời gian này không (trừ chính bản ghi này)
        if (temporaryAbsentRepository.existsByResidentAndDateRangeAndIdNot(
                temporaryAbsent.getResident(), request.getStartDate(), request.getEndDate(), temporaryAbsentId)) {
            throw new RuntimeException("Thành viên đã đăng ký tạm vắng trong khoảng thời gian này");
        }

        temporaryAbsent.setStartDate(request.getStartDate());
        temporaryAbsent.setEndDate(request.getEndDate());
        temporaryAbsent.setTemporaryAddress(request.getTemporaryAddress());
        temporaryAbsent.setReason(request.getReason());

        temporaryAbsent = temporaryAbsentRepository.save(temporaryAbsent);

        return new ResidentTemporaryAbsentResponse(
            temporaryAbsent.getTemporaryAbsentId(),
            temporaryAbsent.getResident().getResidentId(),
            temporaryAbsent.getResident().getFullName(),
            temporaryAbsent.getResident().getApartment().getApartmentCode(),
            temporaryAbsent.getStartDate(),
            temporaryAbsent.getEndDate(),
            temporaryAbsent.getTemporaryAddress(),
            temporaryAbsent.getReason()
        );
    }
} 