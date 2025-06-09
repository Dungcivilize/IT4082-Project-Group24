package KTPM.Backend.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import KTPM.Backend.entity.ServiceType;
import KTPM.Backend.entity.ServiceType.ServiceTypeEnum;

public interface ServiceTypeRepository extends JpaRepository<ServiceType, Integer> {
    Optional<ServiceType> findByServiceType(ServiceTypeEnum serviceType);
}
