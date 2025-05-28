package com.KTPM.KTPM.Services;

import com.KTPM.KTPM.DTO.CollectionSummaryByNameDTO;
import com.KTPM.KTPM.DTO.CollectionSummaryDTO;
import com.KTPM.KTPM.Repositories.PaymentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CollectionSummaryService {

    private final PaymentRepository paymentRepository;

    public CollectionSummaryService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    public List<CollectionSummaryByNameDTO> getCollectionSummaryByName() {
        return paymentRepository.getSummaryByCpName();
    }
}
