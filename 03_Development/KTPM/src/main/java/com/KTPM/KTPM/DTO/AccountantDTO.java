package com.KTPM.KTPM.DTO;

public class AccountantDTO {
    private Long accountantId;
    private Double salary;

    public AccountantDTO() {
    }

    public AccountantDTO(Long accountantId, Double salary) {
        this.accountantId = accountantId;
        this.salary = salary;
    }

    public Long getAccountantId() {
        return accountantId;
    }

    public void setAccountantId(Long accountantId) {
        this.accountantId = accountantId;
    }

    public Double getSalary() {
        return salary;
    }

    public void setSalary(Double salary) {
        this.salary = salary;
    }
}
