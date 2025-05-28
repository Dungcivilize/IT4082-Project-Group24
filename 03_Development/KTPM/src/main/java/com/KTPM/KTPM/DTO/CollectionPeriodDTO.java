package com.KTPM.KTPM.DTO;

import java.time.LocalDate;

public class CollectionPeriodDTO {
    private Long cpId;
    private String cpName;
    private LocalDate collectionDate;

    public CollectionPeriodDTO(Long cpId, String cpName, LocalDate collectionDate) {
        this.cpId = cpId;
        this.cpName = cpName;
        this.collectionDate = collectionDate;
    }

    public Long getCpId() {
        return cpId;
    }

    public String getCpName() {
        return cpName;
    }

    public LocalDate getCollectionDate() {
        return collectionDate;
    }
}
