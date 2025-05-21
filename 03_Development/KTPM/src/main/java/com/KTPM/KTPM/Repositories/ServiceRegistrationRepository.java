package com.KTPM.KTPM.Repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

import com.KTPM.KTPM.Models.ServiceRegistration;

public interface ServiceRegistrationRepository extends JpaRepository<ServiceRegistration, Long> {
    Optional<ServiceRegistration> findByResident_ResidentIdAndService_ServiceId(Long residentId, Long serviceId);


    List<ServiceRegistration> findByResident_ResidentId(Long residentId);
    @Modifying
    @Transactional
    @Query("DELETE FROM ServiceRegistration s WHERE s.resident.residentId = :residentId AND s.service.serviceId = :serviceId")
    void deleteByResidentIdAndServiceId(@Param("residentId") Long residentId, @Param("serviceId") Long serviceId);
}
