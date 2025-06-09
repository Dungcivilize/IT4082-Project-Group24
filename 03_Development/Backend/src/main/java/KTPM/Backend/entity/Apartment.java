package KTPM.Backend.entity;

import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "apartment")
@Data
@NoArgsConstructor
public class Apartment {
    public enum Status {
        occupied,
        empty
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "apartment_id")
    private Integer apartmentId;

    @Column(name = "apartment_code", nullable = false, unique = true)
    private String apartmentCode;

    @Column(name = "floor", nullable = false)
    private Integer floor;

    @Column(name = "area")
    private BigDecimal area;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status = Status.empty;

    @OneToMany(mappedBy = "apartment")
    @JsonManagedReference(value = "apartment-ownerships")
    private List<ApartmentOwnership> ownerships;
} 