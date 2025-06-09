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
import KTPM.Backend.entity.ApartmentOwnership;
import KTPM.Backend.repository.UserRepository;
import KTPM.Backend.repository.VehicleRepository;
import KTPM.Backend.repository.ResidentRepository;
import KTPM.Backend.service.ApartmentOwnershipService;

@Service
public class ResidentVehicleService {
    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ResidentRepository residentRepository;

    @Autowired
    private ApartmentOwnershipService apartmentOwnershipService;

    public List<ResidentVehicleResponse> getVehicles(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));

        ApartmentOwnership ownership = apartmentOwnershipService.getCurrentOwnershipByUserId(userId);

        List<Vehicle> vehicles = vehicleRepository.findByOwnership(ownership);
        return vehicles.stream()
                .map(v -> new ResidentVehicleResponse(
                    v.getVehicleId(),
                    v.getLicensePlate(),
                    v.getType(),
                    v.getOwnership().getApartment().getApartmentCode(),
                    v.getOwner().getFullName(),
                    v.getOwner().getResidentId()
                ))
                .collect(Collectors.toList());
    }

    public ResidentVehicleResponse createVehicle(Integer userId, ResidentVehicleRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));

        ApartmentOwnership ownership = apartmentOwnershipService.getCurrentOwnershipByUserId(userId);

        // Tìm chủ xe theo ID
        Resident owner = residentRepository.findById(request.getOwnerId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy thông tin chủ xe"));

        // Kiểm tra xem chủ xe có thuộc quyền sở hữu không
        if (!owner.getOwnership().getOwnershipId().equals(ownership.getOwnershipId())) {
            throw new RuntimeException("Chủ xe không thuộc căn hộ này");
        }

        // Kiểm tra trạng thái của chủ xe
        if (owner.getStatus() != Resident.ResidentStatus.living) {
            throw new RuntimeException("Chủ xe không còn ở trong căn hộ");
        }

        // Kiểm tra biển số xe đã tồn tại chưa
        if (vehicleRepository.existsByLicensePlate(request.getLicensePlate())) {
            throw new RuntimeException("Biển số xe đã tồn tại trong hệ thống");
        }

        Vehicle vehicle = new Vehicle();
        vehicle.setLicensePlate(request.getLicensePlate());
        vehicle.setType(request.getType());
        vehicle.setOwnership(ownership);
        vehicle.setOwner(owner);

        vehicle = vehicleRepository.save(vehicle);

        return new ResidentVehicleResponse(
            vehicle.getVehicleId(),
            vehicle.getLicensePlate(),
            vehicle.getType(),
            vehicle.getOwnership().getApartment().getApartmentCode(),
            vehicle.getOwner().getFullName(),
            vehicle.getOwner().getResidentId()
        );
    }

    public void deleteVehicle(Integer userId, Integer vehicleId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));

        ApartmentOwnership ownership = apartmentOwnershipService.getCurrentOwnershipByUserId(userId);

        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy phương tiện"));

        // Kiểm tra xem vehicle có thuộc quyền sở hữu của user không
        if (!vehicle.getOwnership().getOwnershipId().equals(ownership.getOwnershipId())) {
            throw new RuntimeException("Không có quyền xóa phương tiện này");
        }

        vehicleRepository.delete(vehicle);
    }

    public ResidentVehicleResponse updateVehicle(Integer userId, Integer vehicleId, ResidentVehicleRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));

        ApartmentOwnership ownership = apartmentOwnershipService.getCurrentOwnershipByUserId(userId);

        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy phương tiện"));

        // Kiểm tra xem vehicle có thuộc quyền sở hữu của user không
        if (!vehicle.getOwnership().getOwnershipId().equals(ownership.getOwnershipId())) {
            throw new RuntimeException("Không có quyền cập nhật phương tiện này");
        }

        // Kiểm tra biển số xe đã tồn tại chưa (nếu có thay đổi)
        if (!request.getLicensePlate().equals(vehicle.getLicensePlate()) &&
            vehicleRepository.existsByLicensePlate(request.getLicensePlate())) {
            throw new RuntimeException("Biển số xe đã tồn tại trong hệ thống");
        }

        if (request.getOwnerId() != null && !request.getOwnerId().equals(vehicle.getOwner().getResidentId())) {
            Resident newOwner = residentRepository.findById(request.getOwnerId())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy thông tin chủ xe"));
            
            // Kiểm tra xem chủ xe mới có thuộc quyền sở hữu không
            if (!newOwner.getOwnership().getOwnershipId().equals(ownership.getOwnershipId())) {
                throw new RuntimeException("Chủ xe không thuộc căn hộ này");
            }

            // Kiểm tra trạng thái của chủ xe mới
            if (newOwner.getStatus() != Resident.ResidentStatus.living) {
                throw new RuntimeException("Chủ xe mới không còn ở trong căn hộ");
            }
            
            vehicle.setOwner(newOwner);
        }

        vehicle.setLicensePlate(request.getLicensePlate());
        vehicle.setType(request.getType());

        vehicle = vehicleRepository.save(vehicle);

        return new ResidentVehicleResponse(
            vehicle.getVehicleId(),
            vehicle.getLicensePlate(),
            vehicle.getType(),
            vehicle.getOwnership().getApartment().getApartmentCode(),
            vehicle.getOwner().getFullName(),
            vehicle.getOwner().getResidentId()
        );
    }
} 