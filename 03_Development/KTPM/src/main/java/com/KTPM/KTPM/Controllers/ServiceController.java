package com.KTPM.KTPM.Controllers;

import com.KTPM.KTPM.Models.Service;
import com.KTPM.KTPM.Repositories.ServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/service")
public class ServiceController {

    @Autowired
    private ServiceRepository serviceRepository;

    // Lấy tất cả dịch vụ
    @GetMapping
    public List<Service> getAllServices() {
        return serviceRepository.findAll();
    }

    // Thêm dịch vụ mới
    @PostMapping
    public Service createService(@RequestBody Service service) {
        return serviceRepository.save(service);
    }

    // Cập nhật dịch vụ theo ID
    @PutMapping("/{id}")
    public ResponseEntity<Service> updateService(@PathVariable Long id, @RequestBody Service serviceDetails) {
        Optional<Service> optionalService = serviceRepository.findById(id);
        if (!optionalService.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Service service = optionalService.get();
        service.setServiceName(serviceDetails.getServiceName());
        service.setDescription(serviceDetails.getDescription());
        service.setFee(serviceDetails.getFee());

        Service updatedService = serviceRepository.save(service);
        return ResponseEntity.ok(updatedService);
    }

    // Xóa dịch vụ theo ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteService(@PathVariable Long id) {
        Optional<Service> optionalService = serviceRepository.findById(id);
        if (!optionalService.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        serviceRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
