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
    public enum Status {
        occupied, empty
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "apartment_id")
    private Integer apartmentId;

    @Column(name = "apartment_code", nullable = false, unique = true, length = 10)
    private String apartmentCode;

    @Column(nullable = false)
    private Integer floor;

    @Column(precision = 10, scale = 2)
    private BigDecimal area;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status = Status.empty;
} 