package KTPM.Backend.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "payment_detail")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDetail {
    public enum Status {
        UNPAID,         // Chưa thanh toán
        PROCESSING,     // Đang xử lý thanh toán
        PAID           // Đã thanh toán
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_detail_id")
    private Integer paymentDetailId;

    @ManyToOne
    @JoinColumn(name = "payment_period_id", nullable = false)
    @JsonBackReference(value = "payment-period-details")
    private PaymentPeriod paymentPeriod;

    @ManyToOne
    @JoinColumn(name = "ownership_id", nullable = false)
    @JsonBackReference(value = "ownership-details")
    private ApartmentOwnership ownership;

    @ManyToOne
    @JoinColumn(name = "service_type_id", nullable = false)
    private ServiceType serviceType;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Column(name = "price", nullable = false)
    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;

    @Column(name = "transaction_code")
    private String transactionCode;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "paid_at")
    private LocalDateTime paidAt;

    @Column(name = "note",nullable = true)
    private String note;

    @PrePersist
    @PreUpdate
    private void calculatePrice() {
        if (this.amount != null && this.serviceType != null && this.serviceType.getUnitPrice() != null) {
            this.price = this.amount.multiply(this.serviceType.getUnitPrice());
        }
    }
} 