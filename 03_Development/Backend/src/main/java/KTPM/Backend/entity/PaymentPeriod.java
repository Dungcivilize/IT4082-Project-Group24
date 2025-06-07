package KTPM.Backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "payment_period")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentPeriod {
    public enum PaymentPeriodStatus {
        collecting, completed
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_period_id")
    private Integer paymentPeriodId;

    @Column(name = "month", nullable = false)
    private Integer month;

    @Column(name = "year", nullable = false)
    private Integer year;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private PaymentPeriodStatus status = PaymentPeriodStatus.collecting;

    @Column(name = "note", columnDefinition = "TEXT")
    private String note;
} 