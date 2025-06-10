package KTPM.Backend.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import KTPM.Backend.entity.Apartment;
import KTPM.Backend.entity.ApartmentOwnership;
import KTPM.Backend.entity.User;
import KTPM.Backend.repository.ApartmentOwnershipRepository;
import KTPM.Backend.repository.ApartmentRepository;
import KTPM.Backend.repository.UserRepository;

@Service
public class ApartmentOwnershipService {
    @Autowired
    private ApartmentOwnershipRepository apartmentOwnershipRepository;

    @Autowired
    private ApartmentRepository apartmentRepository;

    @Autowired
    private UserRepository userRepository;

    public List<ApartmentOwnership> getAllOwnerships() {
        return apartmentOwnershipRepository.findAll();
    }

    public List<ApartmentOwnership> getLatestOwnerships() {
        return apartmentOwnershipRepository.findLatestOwnershipForEachApartment();
    }

    public ApartmentOwnership getOwnershipById(Integer id) {
        return apartmentOwnershipRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy thông tin sở hữu"));
    }

    public List<ApartmentOwnership> getOwnershipsByApartmentId(Integer apartmentId) {
        return apartmentOwnershipRepository.findByApartmentApartmentId(apartmentId);
    }

    public List<ApartmentOwnership> getOwnershipsByUserId(Integer userId) {
        return apartmentOwnershipRepository.findByUserUserId(userId);
    }

    @Transactional
    public ApartmentOwnership assignApartmentToUser(Integer apartmentId, Integer userId, LocalDate startDate) {
        // Kiểm tra căn hộ có tồn tại không
        Apartment apartment = apartmentRepository.findById(apartmentId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy căn hộ"));

        // Kiểm tra người dùng có tồn tại không
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));

        // Kiểm tra căn hộ đã có người ở chưa
        apartmentOwnershipRepository.findCurrentOwnershipByApartmentId(apartmentId)
                .ifPresent(ownership -> {
                    throw new RuntimeException("Căn hộ này đã có người ở");
                });

        // Tạo bản ghi sở hữu mới
        ApartmentOwnership ownership = new ApartmentOwnership();
        ownership.setApartment(apartment);
        ownership.setUser(user);
        ownership.setStartDate(startDate);
        ownership.setStatus(ApartmentOwnership.Status.active);

        // Cập nhật trạng thái căn hộ
        apartment.setStatus(Apartment.Status.occupied);
        apartmentRepository.save(apartment);

        return apartmentOwnershipRepository.save(ownership);
    }

    @Transactional
    public void endOwnership(Integer ownershipId, LocalDate endDate) {
        ApartmentOwnership ownership = apartmentOwnershipRepository.findById(ownershipId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy thông tin sở hữu"));

        if (ownership.getEndDate() != null) {
            throw new RuntimeException("Thông tin sở hữu này đã kết thúc");
        }

        ownership.setEndDate(endDate);
        ownership.setStatus(ApartmentOwnership.Status.inactive);

        // Cập nhật trạng thái căn hộ
        Apartment apartment = ownership.getApartment();
        apartment.setStatus(Apartment.Status.empty);
        apartmentRepository.save(apartment);

        apartmentOwnershipRepository.save(ownership);
    }

    public ApartmentOwnership getCurrentOwnershipByApartmentId(Integer apartmentId) {
        return apartmentOwnershipRepository.findCurrentOwnershipByApartmentId(apartmentId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy thông tin sở hữu hiện tại của căn hộ"));
    }

    public ApartmentOwnership getCurrentOwnershipByUserId(Integer userId) {
        return apartmentOwnershipRepository.findCurrentOwnershipByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy thông tin sở hữu hiện tại của người dùng"));
    }
} 