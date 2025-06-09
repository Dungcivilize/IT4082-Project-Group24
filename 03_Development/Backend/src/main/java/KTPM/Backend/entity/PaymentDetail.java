package KTPM.Backend.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "payment_detail")
@Data
public class PaymentDetail {
    public enum Status {
        pending, paid
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_detail_id")
    private Integer paymentDetailId;

    @ManyToOne
    @JoinColumn(name = "payment_period_id", nullable = false)
    private PaymentPeriod paymentPeriod;

    @ManyToOne
    @JoinColumn(name = "ownership_id", nullable = false)
    private ApartmentOwnership ownership;

    @ManyToOne
    @JoinColumn(name = "service_type_id", nullable = false)
    private ServiceType serviceType;

    @Column(name = "amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", columnDefinition = "ENUM('pending', 'paid') DEFAULT 'pending'")
    private Status status = Status.pending;

    @Column(name = "created_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt = LocalDateTime.now();

    @OneToOne(mappedBy = "paymentDetail", cascade = CascadeType.ALL)
    private Payment payment;
} 