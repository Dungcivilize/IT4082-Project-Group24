package KTPM.Backend.Admin.service;

import KTPM.Backend.Admin.dto.AdminApartmentDTO;
import KTPM.Backend.entity.Apartment;
import KTPM.Backend.repository.ApartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AdminApartmentService {

    @Autowired
    private ApartmentRepository apartmentRepository;

    // Lấy tất cả căn hộ
    public List<AdminApartmentDTO> getAllApartments() {
        return apartmentRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Lấy căn hộ trống
    public List<AdminApartmentDTO> getEmptyApartments() {
        return apartmentRepository.findByStatus(Apartment.Status.empty)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Lấy căn hộ theo ID
    public Optional<AdminApartmentDTO> getApartmentById(Integer id) {
        return apartmentRepository.findById(id)
                .map(this::convertToDTO);
    }

    // Tạo mới căn hộ
    public AdminApartmentDTO createApartment(AdminApartmentDTO dto) {
        Apartment apartment = convertToEntity(dto);
        Apartment saved = apartmentRepository.save(apartment);
        return convertToDTO(saved);
    }

    // Cập nhật căn hộ
    public Optional<AdminApartmentDTO> updateApartment(Integer id, AdminApartmentDTO dto) {
        if (!apartmentRepository.existsById(id)) {
            return Optional.empty();
        }
        dto.setApartmentId(id);
        Apartment apartment = convertToEntity(dto);
        Apartment updated = apartmentRepository.save(apartment);
        return Optional.of(convertToDTO(updated));
    }

    // Xóa căn hộ
    public boolean deleteApartment(Integer id) {
        if (!apartmentRepository.existsById(id)) {
            return false;
        }
        apartmentRepository.deleteById(id);
        return true;
    }

    // Kiểm tra mã căn hộ đã tồn tại chưa
    public boolean existsByApartmentCode(String apartmentCode) {
        return apartmentRepository.existsByApartmentCode(apartmentCode);
    }

    // Chuyển đổi Entity -> DTO
    private AdminApartmentDTO convertToDTO(Apartment apartment) {
        AdminApartmentDTO dto = new AdminApartmentDTO();
        dto.setApartmentId(apartment.getApartmentId());
        dto.setApartmentCode(apartment.getApartmentCode());
        dto.setFloor(apartment.getFloor());
        dto.setArea(apartment.getArea());
        dto.setStatus(apartment.getStatus());
        return dto;
    }

    // Chuyển đổi DTO -> Entity
    private Apartment convertToEntity(AdminApartmentDTO dto) {
        Apartment apartment = new Apartment();
        apartment.setApartmentId(dto.getApartmentId());
        apartment.setApartmentCode(dto.getApartmentCode());
        apartment.setFloor(dto.getFloor());
        apartment.setArea(dto.getArea());
        apartment.setStatus(dto.getStatus());
        return apartment;
    }
}