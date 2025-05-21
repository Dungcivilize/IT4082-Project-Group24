package com.KTPM.KTPM.Repositories;

import com.KTPM.KTPM.Models.Citizen;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CitizenRepository extends JpaRepository<Citizen,Long> {
    Optional<Citizen> findByUser_UserId(Long userId);
}
