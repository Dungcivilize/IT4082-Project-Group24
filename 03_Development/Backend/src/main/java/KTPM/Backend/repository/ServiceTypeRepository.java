package KTPM.Backend.repository;

import KTPM.Backend.entity.ServiceType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceTypeRepository extends JpaRepository<ServiceType, Long> {
    // Có thể thêm phương thức nếu bạn cần filter theo loại hoặc tên dịch vụ
}
