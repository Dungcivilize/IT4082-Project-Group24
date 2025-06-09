package KTPM.Backend.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "payment")
@Data
@NoArgsConstructor
public class Payment {
    public enum PaymentStatus {
        PAID,
        UNPAID,
        PROCESSING
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private Integer paymentId;

    @OneToOne
    @JoinColumn(name = "payment_detail_id", nullable = false)
    @JsonBackReference(value = "payment-detail")
    private PaymentDetail paymentDetail;

    @Column(name = "price", nullable = false)
    private BigDecimal price;

    @Column(name = "paid_at")
    private LocalDateTime paidAt;

    @Column(name = "transaction_code")
    private String transactionCode;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", columnDefinition = "ENUM('PAID', 'UNPAID', 'PROCESSING') DEFAULT 'UNPAID'")
    private PaymentStatus status = PaymentStatus.UNPAID;

    @Column(name = "note")
    private String note;
} 