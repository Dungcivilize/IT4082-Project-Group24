package KTPM.Backend.entity;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "apartment_ownership")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApartmentOwnership {
    public enum Status {
        active, inactive
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ownership_id")
    private Integer ownershipId;

    @ManyToOne
    @JoinColumn(name = "apartment_id", nullable = false)
    @JsonBackReference(value = "apartment-ownerships")
    private Apartment apartment;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference(value = "user-ownerships")
    private User user;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", columnDefinition = "ENUM('active', 'inactive') DEFAULT 'active'")
    private Status status = Status.active;

    @OneToMany(mappedBy = "ownership")
    @JsonManagedReference(value = "ownership-details")
    private List<PaymentDetail> paymentDetails;
} 