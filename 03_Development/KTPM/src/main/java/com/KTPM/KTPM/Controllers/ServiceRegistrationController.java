package com.KTPM.KTPM.Controllers;

import com.KTPM.KTPM.DTO.DeleteServiceRequest;
import com.KTPM.KTPM.DTO.ServiceRegistrationRequest;
import com.KTPM.KTPM.Models.Resident;
import com.KTPM.KTPM.Models.Service;
import com.KTPM.KTPM.Models.ServiceRegistration;
import com.KTPM.KTPM.Repositories.ResidentRepository;
import com.KTPM.KTPM.Repositories.ServiceRegistrationRepository;
import com.KTPM.KTPM.Repositories.ServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/service-registrations")
public class ServiceRegistrationController {

    @Autowired
    private ServiceRegistrationRepository registrationRepo;

    @Autowired
    private ResidentRepository residentRepo;

    @Autowired
    private ServiceRepository serviceRepo;

    @PostMapping("/register")
    public ResponseEntity<?> registerService(@RequestBody ServiceRegistrationRequest request) {
        Optional<Resident> residentOpt = residentRepo.findById(request.getResidentId());
        Optional<Service> serviceOpt = serviceRepo.findById(request.getServiceId());

        if (residentOpt.isEmpty() || serviceOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Resident or Service not found");
        }

        ServiceRegistration registration = new ServiceRegistration();
        registration.setResident(residentOpt.get());
        registration.setService(serviceOpt.get());
        registration.setQuantity(request.getQuantity());

        registrationRepo.save(registration);
        return ResponseEntity.ok("Service registered successfully");
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteService(@RequestBody DeleteServiceRequest request) {
        Long residentId = request.getResidentId();
        Long serviceId = request.getServiceId();

        Optional<ServiceRegistration> registrationOpt =
                registrationRepo.findByResident_ResidentIdAndService_ServiceId(residentId, serviceId);

        if (registrationOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Service registration not found for given resident and service");
        }

        registrationRepo.deleteByResidentIdAndServiceId(residentId, serviceId);

        return ResponseEntity.ok("Service registration deleted successfully");
    }
}

