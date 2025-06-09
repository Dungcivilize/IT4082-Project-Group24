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
import KTPM.Backend.entity.ApartmentOwnership;
import KTPM.Backend.repository.PaymentDetailRepository;
import KTPM.Backend.repository.PaymentRepository;
import KTPM.Backend.repository.UserRepository;
import KTPM.Backend.service.ApartmentOwnershipService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ResidentPaymentService {
    @Autowired
    private PaymentDetailRepository paymentDetailRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ApartmentOwnershipService apartmentOwnershipService;

    public List<ResidentPaymentDetailResponse> getPendingPayments(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));

        ApartmentOwnership ownership = apartmentOwnershipService.getCurrentOwnershipByUserId(userId);

        List<PaymentDetail> paymentDetails = paymentDetailRepository.findByOwnershipAndStatus(ownership, PaymentDetail.Status.pending);
        return convertToResponse(paymentDetails);
    }

    public List<ResidentPaymentDetailResponse> getPaymentHistory(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));

        ApartmentOwnership ownership = apartmentOwnershipService.getCurrentOwnershipByUserId(userId);

        List<PaymentDetail> paymentDetails = paymentDetailRepository.findByOwnershipOwnershipId(ownership.getOwnershipId());
        return convertToResponse(paymentDetails);
    }

    private List<ResidentPaymentDetailResponse> convertToResponse(List<PaymentDetail> paymentDetails) {
        return paymentDetails.stream()
                .map(pd -> new ResidentPaymentDetailResponse(
                    pd.getPaymentDetailId(),
                    pd.getPaymentPeriod().getMonth(),
                    pd.getPaymentPeriod().getYear(),
                    pd.getServiceType().getServiceName(),
                    pd.getAmount(),
                    pd.getServiceType().getUnitPrice(),
                    pd.getAmount().multiply(pd.getServiceType().getUnitPrice()),
                    pd.getStatus(),
                    pd.getPayment() != null ? pd.getPayment().getStatus() : Payment.PaymentStatus.UNPAID,
                    pd.getPayment() != null ? pd.getPayment().getTransactionCode() : null,
                    pd.getPayment() != null ? pd.getPayment().getPaidAt() : null
                ))
                .collect(Collectors.toList());
    }

    public List<ResidentPaymentDetailResponse> getPaymentDetails(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));

        ApartmentOwnership ownership = apartmentOwnershipService.getCurrentOwnershipByUserId(userId);

        List<PaymentDetail> paymentDetails = paymentDetailRepository.findByOwnershipOwnershipId(ownership.getOwnershipId());
        return paymentDetails.stream()
                .map(pd -> new ResidentPaymentDetailResponse(
                    pd.getPaymentDetailId(),
                    pd.getPaymentPeriod().getMonth(),
                    pd.getPaymentPeriod().getYear(),
                    pd.getServiceType().getServiceName(),
                    pd.getAmount(),
                    pd.getServiceType().getUnitPrice(),
                    pd.getAmount().multiply(pd.getServiceType().getUnitPrice()),
                    pd.getStatus(),
                    pd.getPayment() != null ? pd.getPayment().getStatus() : null,
                    pd.getPayment() != null ? pd.getPayment().getTransactionCode() : null,
                    pd.getPayment() != null ? pd.getPayment().getPaidAt() : null
                ))
                .collect(Collectors.toList());
    }

    @Transactional
    public ResidentPaymentDetailResponse submitPayment(Integer userId, ResidentPaymentRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));

        ApartmentOwnership ownership = apartmentOwnershipService.getCurrentOwnershipByUserId(userId);

        PaymentDetail paymentDetail = paymentDetailRepository.findById(request.getPaymentDetailId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy chi tiết thanh toán"));

        // Kiểm tra quyền thanh toán
        if (!paymentDetail.getOwnership().getOwnershipId().equals(ownership.getOwnershipId())) {
            throw new RuntimeException("Không có quyền thanh toán khoản phí này");
        }

        // Kiểm tra trạng thái payment detail
        if (paymentDetail.getStatus() != PaymentDetail.Status.pending) {
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

        payment = paymentRepository.save(payment);

        return new ResidentPaymentDetailResponse(
            paymentDetail.getPaymentDetailId(),
            paymentDetail.getPaymentPeriod().getMonth(),
            paymentDetail.getPaymentPeriod().getYear(),
            paymentDetail.getServiceType().getServiceName(),
            paymentDetail.getAmount(),
            paymentDetail.getServiceType().getUnitPrice(),
            totalPrice,
            paymentDetail.getStatus(),
            payment.getStatus(),
            payment.getTransactionCode(),
            payment.getPaidAt()
        );
    }
} 