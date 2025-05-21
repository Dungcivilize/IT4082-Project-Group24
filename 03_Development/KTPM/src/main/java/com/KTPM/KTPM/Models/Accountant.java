package com.KTPM.KTPM.Models;

import jakarta.persistence.*;

@Entity
@Table(name = "accountant")
public class Accountant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "accountant_id")
    private Long accountantId;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "salary", nullable = false)
    private Double salary;

    // Constructors
    public Accountant() {}

    public Accountant(User user, Double salary) {
        this.user = user;
        this.salary = salary;
    }

    // Getters and Setters
    public Long getAccountantId() {
        return accountantId;
    }

    public void setAccountantId(Long accountantId) {
        this.accountantId = accountantId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Double getSalary() {
        return salary;
    }

    public void setSalary(Double salary) {
        this.salary = salary;
    }
}