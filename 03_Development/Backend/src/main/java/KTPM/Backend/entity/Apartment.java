package KTPM.Backend.entity;

import java.math.BigDecimal;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "apartment")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Apartment {
    public enum ApartmentStatus {
        occupied, empty
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "apartment_id")
    private Integer apartmentId;

    @Column(name = "apartment_code", nullable = false, length = 10, unique = true)
    private String apartmentCode;

    @Column(name = "floor", nullable = false)
    private Integer floor;

    @Column(name = "area", precision = 10, scale = 2)
    private BigDecimal area;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ApartmentStatus status = ApartmentStatus.empty;
} 