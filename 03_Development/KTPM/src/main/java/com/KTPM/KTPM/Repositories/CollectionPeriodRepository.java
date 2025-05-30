package com.KTPM.KTPM.Repositories;

import com.KTPM.KTPM.Models.CollectionPeriod;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CollectionPeriodRepository extends JpaRepository<CollectionPeriod, Long> {
    Optional<CollectionPeriod> findByCpName(String cpName);
}
