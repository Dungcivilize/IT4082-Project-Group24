package com.KTPM.KTPM.Models;

import jakarta.persistence.*;

@Entity
@Table(name = "accommodation_information")
public class AccommodationInformation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ai_id")
    private Long aiId;

    @ManyToOne
    @JoinColumn(name = "citizen_id", nullable = false)
    private Citizen citizen;

    @Column(name = "description", nullable = false, length = 255)
    private String description;

    // Constructors
    public AccommodationInformation() {
    }

    public AccommodationInformation(Citizen citizen, String description) {
        this.citizen = citizen;
        this.description = description;
    }

    // Getters and Setters
    public Long getAiId() {
        return aiId;
    }

    public void setAiId(Long aiId) {
        this.aiId = aiId;
    }

    public Citizen getCitizen() {
        return citizen;
    }

    public void setCitizen(Citizen citizen) {
        this.citizen = citizen;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
