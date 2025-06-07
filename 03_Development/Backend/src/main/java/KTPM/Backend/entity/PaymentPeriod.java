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
@Table(name = "payment_period")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentPeriod {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_period_id")
    private Integer paymentPeriodId;

    @Column(name = "month", nullable = false)
    private Integer month;

    @Column(name = "year", nullable = false)
    private Integer year;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private PaymentPeriodStatus status = PaymentPeriodStatus.COLLECTING;

    @Column(name = "note", columnDefinition = "TEXT")
    private String note;

    public enum PaymentPeriodStatus {
        COLLECTING,
        COMPLETED
    }
} 