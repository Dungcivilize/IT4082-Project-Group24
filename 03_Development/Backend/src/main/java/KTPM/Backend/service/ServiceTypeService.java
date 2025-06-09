package KTPM.Backend.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import KTPM.Backend.dto.ServiceTypeRequest;
import KTPM.Backend.dto.ServiceTypeResponse;
import KTPM.Backend.entity.ServiceType;
import KTPM.Backend.repository.PaymentDetailRepository;
import KTPM.Backend.repository.ServiceTypeRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ServiceTypeService {
    @Autowired
    private ServiceTypeRepository serviceTypeRepository;

    @Autowired
    private PaymentDetailRepository paymentDetailRepository;

    public List<ServiceType> getAllServiceTypes() {
        return serviceTypeRepository.findAll();
    }

    public ServiceTypeResponse getServiceTypeById(Integer id) {
        ServiceType serviceType = serviceTypeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy loại dịch vụ"));
        return ServiceTypeResponse.fromEntity(serviceType);
    }

    @Transactional
    public ServiceTypeResponse createServiceType(ServiceTypeRequest request) {
        ServiceType serviceType = new ServiceType();
        updateServiceTypeFromRequest(serviceType, request);
        serviceType = serviceTypeRepository.save(serviceType);
        return ServiceTypeResponse.fromEntity(serviceType);
    }

    @Transactional
    public ServiceTypeResponse updateServiceType(Integer id, ServiceTypeRequest request) {
        ServiceType serviceType = serviceTypeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy loại dịch vụ"));
        updateServiceTypeFromRequest(serviceType, request);
        serviceType = serviceTypeRepository.save(serviceType);
        return ServiceTypeResponse.fromEntity(serviceType);
    }

    @Transactional
    public void deleteServiceType(Integer id) {
        ServiceType serviceType = serviceTypeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy loại dịch vụ"));

        // Kiểm tra xem dịch vụ có đang được sử dụng không
        if (paymentDetailRepository.existsByServiceType(serviceType)) {
            throw new RuntimeException("Không thể xóa dịch vụ đang được sử dụng");
        }

        serviceTypeRepository.delete(serviceType);
    }

    private void updateServiceTypeFromRequest(ServiceType serviceType, ServiceTypeRequest request) {
        serviceType.setServiceName(request.getServiceName());
        serviceType.setServiceType(request.getServiceType());
        serviceType.setUnitPrice(request.getUnitPrice());
    }
} 