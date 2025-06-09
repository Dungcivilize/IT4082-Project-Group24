package KTPM.Backend.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Arrays;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import KTPM.Backend.dto.ResidentPaymentDetailResponse;
import KTPM.Backend.dto.ResidentPaymentRequest;
import KTPM.Backend.entity.PaymentDetail;
import KTPM.Backend.entity.User;
import KTPM.Backend.entity.ApartmentOwnership;
import KTPM.Backend.repository.PaymentDetailRepository;
import KTPM.Backend.repository.UserRepository;
import KTPM.Backend.service.ApartmentOwnershipService;
import lombok.RequiredArgsConstructor;
import KTPM.Backend.dto.PaymentDetailRequest;
import KTPM.Backend.entity.*;
import KTPM.Backend.repository.*;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class ResidentPaymentService {
    @Autowired
    private PaymentDetailRepository paymentDetailRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ApartmentOwnershipService apartmentOwnershipService;

    @Autowired
    private ServiceTypeRepository serviceTypeRepository;

    @Autowired
    private ApartmentOwnershipRepository apartmentOwnershipRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private PaymentPeriodRepository paymentPeriodRepository;

    public List<ResidentPaymentDetailResponse> getPendingPayments(Integer userId) {
        List<ApartmentOwnership> ownerships = apartmentOwnershipService.getOwnershipsByUserId(userId);
        List<PaymentDetail> pendingPayments = paymentDetailRepository.findByOwnershipInAndStatusIn(
                ownerships, Arrays.asList(PaymentDetail.Status.UNPAID, PaymentDetail.Status.PROCESSING));
        return convertToResponse(pendingPayments);
    }

    public List<ResidentPaymentDetailResponse> getPaymentHistory(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));

        ApartmentOwnership ownership = apartmentOwnershipService.getCurrentOwnershipByUserId(userId);

        List<PaymentDetail> paymentDetails = paymentDetailRepository.findByOwnershipOwnershipId(ownership.getOwnershipId());
        return convertToResponse(paymentDetails);
    }

    private List<ResidentPaymentDetailResponse> convertToResponse(List<PaymentDetail> paymentDetails) {
        return paymentDetails.stream().map(pd -> {
            return ResidentPaymentDetailResponse.builder()
                .paymentDetailId(pd.getPaymentDetailId())
                .periodInfo(String.format("%02d/%d", pd.getPaymentPeriod().getMonth(), pd.getPaymentPeriod().getYear()))
                .serviceTypeName(pd.getServiceType().getServiceName())
                .amount(pd.getAmount())
                .unitPrice(pd.getServiceType().getUnitPrice())
                .price(pd.getAmount().multiply(pd.getServiceType().getUnitPrice()))
                .status(pd.getStatus())
                .transactionCode(pd.getTransactionCode())
                .createdAt(pd.getCreatedAt())
                .paidAt(pd.getPaidAt())
                .apartmentCode(pd.getOwnership().getApartment().getApartmentCode())
                .ownerName(pd.getOwnership().getUser().getFullName())
                .note(pd.getNote())
                .build();
        }).collect(Collectors.toList());
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

        // Kiểm tra trạng thái
        if (paymentDetail.getStatus() != PaymentDetail.Status.UNPAID) {
            throw new RuntimeException("Khoản phí này không ở trạng thái chờ thanh toán");
        }

        // Cập nhật thông tin thanh toán
        paymentDetail.setTransactionCode(request.getTransactionCode());
        paymentDetail.setStatus(PaymentDetail.Status.PROCESSING);
        paymentDetail.setPaidAt(LocalDateTime.now());

        // Lưu thay đổi
        paymentDetail = paymentDetailRepository.save(paymentDetail);

        return ResidentPaymentDetailResponse.builder()
            .paymentDetailId(paymentDetail.getPaymentDetailId())
            .periodInfo(String.format("%02d/%d", paymentDetail.getPaymentPeriod().getMonth(), paymentDetail.getPaymentPeriod().getYear()))
            .serviceTypeName(paymentDetail.getServiceType().getServiceName())
            .amount(paymentDetail.getAmount())
            .unitPrice(paymentDetail.getServiceType().getUnitPrice())
            .price(paymentDetail.getAmount().multiply(paymentDetail.getServiceType().getUnitPrice()))
            .status(paymentDetail.getStatus())
            .transactionCode(paymentDetail.getTransactionCode())
            .createdAt(paymentDetail.getCreatedAt())
            .paidAt(paymentDetail.getPaidAt())
            .apartmentCode(paymentDetail.getOwnership().getApartment().getApartmentCode())
            .ownerName(paymentDetail.getOwnership().getUser().getFullName())
            .note(paymentDetail.getNote())
            .build();
    }

    @Transactional
    public PaymentDetail createPaymentDetail(PaymentDetailRequest request) {
        ServiceType serviceType = serviceTypeRepository.findById(request.getServiceTypeId())
                .orElseThrow(() -> new EntityNotFoundException("Service type not found"));

        ApartmentOwnership ownership = apartmentOwnershipRepository.findById(request.getOwnershipId())
                .orElseThrow(() -> new EntityNotFoundException("Apartment ownership not found"));

        PaymentPeriod paymentPeriod = paymentPeriodRepository.findById(request.getPaymentPeriodId())
                .orElseThrow(() -> new EntityNotFoundException("Payment period not found"));

        PaymentDetail paymentDetail = new PaymentDetail();
        paymentDetail.setServiceType(serviceType);
        paymentDetail.setOwnership(ownership);
        paymentDetail.setPaymentPeriod(paymentPeriod);
        paymentDetail.setStatus(PaymentDetail.Status.UNPAID);
        paymentDetail.setCreatedAt(LocalDateTime.now());

        // Tự động tính toán số tiền cho phí xe máy và ô tô
        if (serviceType.getServiceTypeId() == 3) { // Phí xe máy
            int motorbikes = vehicleRepository.countActiveVehiclesByOwnershipAndType(ownership.getOwnershipId(), Vehicle.VehicleType.motorcycle);
            paymentDetail.setAmount(BigDecimal.valueOf(motorbikes));
        } else if (serviceType.getServiceTypeId() == 4) { // Phí ô tô
            int cars = vehicleRepository.countActiveVehiclesByOwnershipAndType(ownership.getOwnershipId(), Vehicle.VehicleType.car);
            paymentDetail.setAmount(BigDecimal.valueOf(cars));
        } else {
            paymentDetail.setAmount(BigDecimal.valueOf(request.getAmount()));
        }

        return paymentDetailRepository.save(paymentDetail);
    }
} 