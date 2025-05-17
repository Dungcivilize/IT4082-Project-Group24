package com.KTPM.KTPM.Models;
import jakarta.persistence.*;

@Entity
@Table(name = "resident")
public class Resident {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "resident_id")
    private Long residentId;

    @OneToOne
    @JoinColumn(name = "householder_id", nullable = false, referencedColumnName = "citizen_id")
    private Citizen householder;

    @Column(name = "address", nullable = false)
    private String address;

    // --- Getters and Setters ---
    public Long getResidentId() {
        return residentId;
    }

    public void setResidentId(Long residentId) {
        this.residentId = residentId;
    }

    public Citizen getHouseholder() {
        return householder;
    }

    public void setHouseholder(Citizen householder) {
        this.householder = householder;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}

