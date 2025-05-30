package com.KTPM.KTPM.Services;

import com.KTPM.KTPM.DTO.CollectionPeriodDTO;
import com.KTPM.KTPM.Models.CollectionPeriod;
import com.KTPM.KTPM.Repositories.CollectionPeriodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CollectionPeriodService {

    @Autowired
    private CollectionPeriodRepository collectionPeriodRepository;

    public List<CollectionPeriodDTO> getAllCollectionPeriods() {
        List<CollectionPeriod> periods = collectionPeriodRepository.findAll();
        return periods.stream()
                .map(CollectionPeriodDTO::new)  // Sử dụng constructor nhận entity để set đầy đủ
                .collect(Collectors.toList());
    }

}
