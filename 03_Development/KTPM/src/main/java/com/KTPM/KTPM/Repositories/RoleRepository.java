package com.KTPM.KTPM.Repositories;
import com.KTPM.KTPM.Models.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    // Bạn có thể thêm các truy vấn tuỳ chỉnh tại đây
}
