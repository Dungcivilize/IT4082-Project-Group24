package KTPM.Backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import KTPM.Backend.dto.ServiceTypeRequest;
import KTPM.Backend.dto.ServiceTypeResponse;
import KTPM.Backend.entity.ServiceType;
import KTPM.Backend.service.ServiceTypeService;

@RestController
@RequestMapping("/api/service-types")
@CrossOrigin(origins = "http://localhost:5173")
public class ServiceTypeController {
    @Autowired
    private ServiceTypeService serviceTypeService;

    @GetMapping
    public ResponseEntity<List<ServiceType>> getAllServiceTypes() {
        try {
            List<ServiceType> serviceTypes = serviceTypeService.getAllServiceTypes();
            return ResponseEntity.ok(serviceTypes);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServiceTypeResponse> getServiceTypeById(@PathVariable Integer id) {
        return ResponseEntity.ok(serviceTypeService.getServiceTypeById(id));
    }

    @PostMapping
    public ResponseEntity<ServiceTypeResponse> createServiceType(@RequestBody ServiceTypeRequest request) {
        return ResponseEntity.ok(serviceTypeService.createServiceType(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ServiceTypeResponse> updateServiceType(
            @PathVariable Integer id,
            @RequestBody ServiceTypeRequest request) {
        return ResponseEntity.ok(serviceTypeService.updateServiceType(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteServiceType(@PathVariable Integer id) {
        serviceTypeService.deleteServiceType(id);
        return ResponseEntity.ok().build();
    }
} 