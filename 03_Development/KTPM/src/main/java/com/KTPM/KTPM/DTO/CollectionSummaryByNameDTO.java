package com.KTPM.KTPM.DTO;

public class CollectionSummaryByNameDTO {
    private String cpName;
    private Double totalPaid;
    private Double totalUnpaid;
    private Long paidHouseholds;
    private Long unpaidHouseholds;

    public CollectionSummaryByNameDTO(String cpName, Double totalPaid, Double totalUnpaid, Long paidHouseholds, Long unpaidHouseholds) {
        this.cpName = cpName;
        this.totalPaid = totalPaid;
        this.totalUnpaid = totalUnpaid;
        this.paidHouseholds = paidHouseholds;
        this.unpaidHouseholds = unpaidHouseholds;
    }

    // Getters
    public String getCpName() {
        return cpName;
    }

    public Double getTotalPaid() {
        return totalPaid;
    }

    public Double getTotalUnpaid() {
        return totalUnpaid;
    }

    public Long getPaidHouseholds() {
        return paidHouseholds;
    }

    public Long getUnpaidHouseholds() {
        return unpaidHouseholds;
    }
}
