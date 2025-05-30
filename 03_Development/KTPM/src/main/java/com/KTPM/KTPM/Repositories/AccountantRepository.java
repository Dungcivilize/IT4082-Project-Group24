package com.KTPM.KTPM.Repositories;

import com.KTPM.KTPM.Models.Accountant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountantRepository extends JpaRepository<Accountant, Long> {
    Optional<Accountant> findByUser_UserId(Long userId);

}