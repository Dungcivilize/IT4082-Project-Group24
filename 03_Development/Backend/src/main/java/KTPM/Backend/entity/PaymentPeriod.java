package KTPM.Backend.entity;

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
@Table(name = "payment_period")
@Data
@NoArgsConstructor
public class PaymentPeriod {
    public enum Status {
        collecting,
        completed
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
    @Column(name = "status", columnDefinition = "ENUM('collecting', 'completed') DEFAULT 'collecting'")
    private Status status = Status.collecting;

    @Column(name = "note")
    private String note;

    @OneToMany(mappedBy = "paymentPeriod")
    @JsonManagedReference(value = "payment-period-details")
    private List<PaymentDetail> paymentDetails;
} 