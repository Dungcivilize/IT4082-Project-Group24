package KTPM.Backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "apartment")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Apartment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "apartment_id")
    private Integer apartmentId;

    @Column(name = "apartment_code", nullable = false, unique = true, length = 10)
    private String apartmentCode;

    @Column(name = "floor", nullable = false)
    private Integer floor;

    @Column(name = "area")
    private Double area;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private ApartmentStatus status = ApartmentStatus.empty;

    public enum ApartmentStatus {
        occupied,
        empty
    }
} 