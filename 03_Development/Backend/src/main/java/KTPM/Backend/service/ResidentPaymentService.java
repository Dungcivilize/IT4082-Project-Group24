package KTPM.Backend.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import KTPM.Backend.dto.ResidentPaymentDetailResponse;
import KTPM.Backend.dto.ResidentPaymentRequest;
import KTPM.Backend.entity.Payment;
import KTPM.Backend.entity.PaymentDetail;
import KTPM.Backend.entity.User;
import KTPM.Backend.repository.PaymentDetailRepository;
import KTPM.Backend.repository.UserRepository;

@Service
public class ResidentPaymentService {
    @Autowired
    private PaymentDetailRepository paymentDetailRepository;

    @Autowired
    private UserRepository userRepository;

    public List<ResidentPaymentDetailResponse> getPendingPayments(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));

        if (user.getApartment() == null) {
            throw new RuntimeException("Người dùng chưa được gán căn hộ");
        }

        List<PaymentDetail> paymentDetails = paymentDetailRepository.findByApartmentAndStatus(
            user.getApartment(), 
            PaymentDetail.PaymentDetailStatus.pending
        );

        return paymentDetails.stream()
                .map(pd -> {
                    // Tính tổng tiền
                    BigDecimal totalPrice = pd.getAmount().multiply(pd.getServiceType().getUnitPrice());
                    
                    // Lấy thông tin payment
                    Payment payment = pd.getPayment();
                    
                    return new ResidentPaymentDetailResponse(
                        pd.getPaymentDetailId(),
                        pd.getServiceType().getServiceName(),
                        pd.getServiceType().getServiceType().toString(),
                        pd.getAmount(),
                        pd.getServiceType().getUnitPrice(),
                        totalPrice,
                        pd.getStatus(),
                        pd.getCreatedAt(),
                        String.format("%02d/%d", pd.getPaymentPeriod().getMonth(), pd.getPaymentPeriod().getYear()),
                        payment != null ? payment.getStatus().toString() : null,
                        payment != null ? payment.getTransactionCode() : null,
                        payment != null ? payment.getNote() : null
                    );
                })
                .collect(Collectors.toList());
    }

    public List<ResidentPaymentDetailResponse> getPaymentHistory(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));

        if (user.getApartment() == null) {
            throw new RuntimeException("Người dùng chưa được gán căn hộ");
        }

        List<PaymentDetail> paymentDetails = paymentDetailRepository.findByApartment(user.getApartment());

        return paymentDetails.stream()
                .map(pd -> {
                    // Tính tổng tiền
                    BigDecimal totalPrice = pd.getAmount().multiply(pd.getServiceType().getUnitPrice());
                    
                    // Lấy thông tin payment
                    Payment payment = pd.getPayment();
                    
                    return new ResidentPaymentDetailResponse(
                        pd.getPaymentDetailId(),
                        pd.getServiceType().getServiceName(),
                        pd.getServiceType().getServiceType().toString(),
                        pd.getAmount(),
                        pd.getServiceType().getUnitPrice(),
                        totalPrice,
                        pd.getStatus(),
                        pd.getCreatedAt(),
                        String.format("%02d/%d", pd.getPaymentPeriod().getMonth(), pd.getPaymentPeriod().getYear()),
                        payment != null ? payment.getStatus().toString() : null,
                        payment != null ? payment.getTransactionCode() : null,
                        payment != null ? payment.getNote() : null
                    );
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public ResidentPaymentDetailResponse submitPayment(Integer userId, ResidentPaymentRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));

        if (user.getApartment() == null) {
            throw new RuntimeException("Người dùng chưa được gán căn hộ");
        }

        PaymentDetail paymentDetail = paymentDetailRepository.findById(request.getPaymentDetailId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy chi tiết thanh toán"));

        // Kiểm tra quyền thanh toán
        if (!paymentDetail.getApartment().getApartmentId().equals(user.getApartment().getApartmentId())) {
            throw new RuntimeException("Không có quyền thanh toán khoản phí này");
        }

        // Kiểm tra trạng thái payment detail
        if (paymentDetail.getStatus() != PaymentDetail.PaymentDetailStatus.pending) {
            throw new RuntimeException("Khoản phí này không ở trạng thái chờ thanh toán");
        }

        // Tính tổng tiền
        BigDecimal totalPrice = paymentDetail.getAmount().multiply(paymentDetail.getServiceType().getUnitPrice());

        // Tạo hoặc cập nhật payment
        Payment payment = paymentDetail.getPayment();
        if (payment == null) {
            payment = new Payment();
            payment.setPaymentDetail(paymentDetail);
            payment.setPrice(totalPrice);
            paymentDetail.setPayment(payment);
        }

        // Kiểm tra trạng thái payment
        if (payment.getStatus() != Payment.PaymentStatus.UNPAID) {
            throw new RuntimeException("Khoản phí này đã được gửi yêu cầu thanh toán");
        }

        // Cập nhật payment
        payment.setTransactionCode(request.getTransactionCode());
        payment.setPaidAt(LocalDateTime.now());
        payment.setStatus(Payment.PaymentStatus.PROCESSING);

        // Lưu payment detail (sẽ cascade lưu payment)
        paymentDetail = paymentDetailRepository.save(paymentDetail);

        return new ResidentPaymentDetailResponse(
            paymentDetail.getPaymentDetailId(),
            paymentDetail.getServiceType().getServiceName(),
            paymentDetail.getServiceType().getServiceType().toString(),
            paymentDetail.getAmount(),
            paymentDetail.getServiceType().getUnitPrice(),
            totalPrice,
            paymentDetail.getStatus(),
            paymentDetail.getCreatedAt(),
            String.format("%02d/%d", paymentDetail.getPaymentPeriod().getMonth(), paymentDetail.getPaymentPeriod().getYear()),
            payment.getStatus().toString(),
            payment.getTransactionCode(),
            payment.getNote()
        );
    }
} 