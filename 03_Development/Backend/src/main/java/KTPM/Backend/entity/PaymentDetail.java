package KTPM.Backend.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "payment_detail")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDetail {
    public enum PaymentDetailStatus {
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
    @JoinColumn(name = "apartment_id", nullable = false)
    private Apartment apartment;

    @ManyToOne
    @JoinColumn(name = "service_type_id", nullable = false)
    private ServiceType serviceType;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private PaymentDetailStatus status = PaymentDetailStatus.pending;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @OneToOne(mappedBy = "paymentDetail", cascade = CascadeType.ALL)
    private Payment payment;
} 