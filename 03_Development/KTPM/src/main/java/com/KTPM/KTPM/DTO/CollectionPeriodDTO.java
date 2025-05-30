package com.KTPM.KTPM.DTO;

import com.KTPM.KTPM.Models.CollectionPeriod;

import java.time.LocalDate;

public class CollectionPeriodDTO {
    private Long cpId;
    private String cpName;
    private String collectionDate;
    private Long residentId;
    private Long accountantId;

    public CollectionPeriodDTO(CollectionPeriod cp) {
        this.cpId = cp.getCpId();
        this.cpName = cp.getCpName();
        this.collectionDate = cp.getCollectionDate().toString();
        this.residentId = cp.getResident().getResidentId();
        this.accountantId = cp.getAccountant().getAccountantId();
    }

    // Getters
    public Long getCpId() {
        return cpId;
    }

    public String getCpName() {
        return cpName;
    }

    public String getCollectionDate() {
        return collectionDate;
    }

    public Long getResidentId() {
        return residentId;
    }

    public Long getAccountantId() {
        return accountantId;
    }
}
