package KTPM.Backend.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import KTPM.Backend.dto.ResidentVehicleRequest;
import KTPM.Backend.dto.ResidentVehicleResponse;
import KTPM.Backend.entity.User;
import KTPM.Backend.entity.Vehicle;
import KTPM.Backend.entity.Resident;
import KTPM.Backend.repository.UserRepository;
import KTPM.Backend.repository.VehicleRepository;
import KTPM.Backend.repository.ResidentRepository;

@Service
public class ResidentVehicleService {
    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ResidentRepository residentRepository;

    public List<ResidentVehicleResponse> getVehicles(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));

        if (user.getApartment() == null) {
            throw new RuntimeException("Người dùng chưa được gán căn hộ");
        }

        List<Vehicle> vehicles = vehicleRepository.findByApartment(user.getApartment());
        return vehicles.stream()
                .map(v -> new ResidentVehicleResponse(
                    v.getVehicleId(),
                    v.getLicensePlate(),
                    v.getType(),
                    v.getApartment().getApartmentCode(),
                    v.getOwner().getFullName()
                ))
                .collect(Collectors.toList());
    }

    public ResidentVehicleResponse createVehicle(Integer userId, ResidentVehicleRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));

        if (user.getApartment() == null) {
            throw new RuntimeException("Người dùng chưa được gán căn hộ");
        }

        // Tìm chủ sở hữu là owner của căn hộ
        List<Resident> owners = residentRepository.findByApartmentAndResidentType(
            user.getApartment(), 
            Resident.ResidentType.owner
        );
        
        if (owners.isEmpty()) {
            throw new RuntimeException("Không tìm thấy chủ sở hữu của căn hộ");
        }

        // Kiểm tra biển số xe đã tồn tại chưa
        if (vehicleRepository.existsByLicensePlate(request.getLicensePlate())) {
            throw new RuntimeException("Biển số xe đã tồn tại trong hệ thống");
        }

        Vehicle vehicle = new Vehicle();
        vehicle.setLicensePlate(request.getLicensePlate());
        vehicle.setType(request.getType());
        vehicle.setApartment(user.getApartment());
        vehicle.setOwner(owners.get(0)); // Lấy chủ sở hữu đầu tiên

        vehicle = vehicleRepository.save(vehicle);

        return new ResidentVehicleResponse(
            vehicle.getVehicleId(),
            vehicle.getLicensePlate(),
            vehicle.getType(),
            vehicle.getApartment().getApartmentCode(),
            vehicle.getOwner().getFullName()
        );
    }

    public ResidentVehicleResponse updateVehicle(Integer userId, Integer vehicleId, ResidentVehicleRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));

        if (user.getApartment() == null) {
            throw new RuntimeException("Người dùng chưa được gán căn hộ");
        }

        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy phương tiện"));

        // Kiểm tra xem vehicle có thuộc căn hộ của user không
        if (!vehicle.getApartment().getApartmentId().equals(user.getApartment().getApartmentId())) {
            throw new RuntimeException("Không có quyền cập nhật thông tin phương tiện này");
        }

        // Kiểm tra biển số xe mới đã tồn tại chưa (nếu có thay đổi)
        if (!vehicle.getLicensePlate().equals(request.getLicensePlate()) &&
            vehicleRepository.existsByLicensePlate(request.getLicensePlate())) {
            throw new RuntimeException("Biển số xe đã tồn tại trong hệ thống");
        }

        vehicle.setLicensePlate(request.getLicensePlate());
        vehicle.setType(request.getType());

        vehicle = vehicleRepository.save(vehicle);

        return new ResidentVehicleResponse(
            vehicle.getVehicleId(),
            vehicle.getLicensePlate(),
            vehicle.getType(),
            vehicle.getApartment().getApartmentCode(),
            vehicle.getOwner().getFullName()
        );
    }

    public void deleteVehicle(Integer userId, Integer vehicleId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));

        if (user.getApartment() == null) {
            throw new RuntimeException("Người dùng chưa được gán căn hộ");
        }

        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy phương tiện"));

        // Kiểm tra xem vehicle có thuộc căn hộ của user không
        if (!vehicle.getApartment().getApartmentId().equals(user.getApartment().getApartmentId())) {
            throw new RuntimeException("Không có quyền xóa phương tiện này");
        }

        vehicleRepository.delete(vehicle);
    }
} 