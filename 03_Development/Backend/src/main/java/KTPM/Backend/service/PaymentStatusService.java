package KTPM.Backend.service;

import java.util.List;
import java.util.stream.Collectors;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import KTPM.Backend.dto.PaymentStatusUpdateRequest;
import KTPM.Backend.dto.ResidentPaymentDetailResponse;
import KTPM.Backend.entity.PaymentDetail;
import KTPM.Backend.repository.PaymentDetailRepository;
import KTPM.Backend.repository.PaymentPeriodRepository;
import KTPM.Backend.repository.ApartmentOwnershipRepository;

@Service
public class PaymentStatusService {
    @Autowired
    private PaymentDetailRepository paymentDetailRepository;

    @Autowired
    private PaymentPeriodRepository paymentPeriodRepository;

    @Autowired
    private ApartmentOwnershipRepository ownershipRepository;

    public List<ResidentPaymentDetailResponse> getAllPayments(Integer paymentPeriodId, Integer ownershipId) {
        List<PaymentDetail> paymentDetails;

        if (paymentPeriodId != null && ownershipId != null) {
            paymentDetails = paymentDetailRepository.findByPaymentPeriodPaymentPeriodIdAndOwnershipOwnershipId(
                paymentPeriodId, ownershipId);
        } else if (paymentPeriodId != null) {
            paymentDetails = paymentDetailRepository.findByPaymentPeriodPaymentPeriodId(paymentPeriodId);
        } else if (ownershipId != null) {
            paymentDetails = paymentDetailRepository.findByOwnershipOwnershipId(ownershipId);
        } else {
            paymentDetails = paymentDetailRepository.findAll();
        }

        return convertToResponse(paymentDetails);
    }

    public List<ResidentPaymentDetailResponse> getPaymentsByStatus(PaymentDetail.Status status, Integer paymentPeriodId, Integer ownershipId) {
        List<PaymentDetail> paymentDetails;

        if (paymentPeriodId != null && ownershipId != null) {
            paymentDetails = paymentDetailRepository.findByStatusAndPaymentPeriodPaymentPeriodIdAndOwnershipOwnershipId(
                status, paymentPeriodId, ownershipId);
        } else if (paymentPeriodId != null) {
            paymentDetails = paymentDetailRepository.findByStatusAndPaymentPeriodPaymentPeriodId(status, paymentPeriodId);
        } else if (ownershipId != null) {
            paymentDetails = paymentDetailRepository.findByStatusAndOwnershipOwnershipId(status, ownershipId);
        } else {
            paymentDetails = paymentDetailRepository.findByStatus(status);
        }

        return convertToResponse(paymentDetails);
    }

    private List<ResidentPaymentDetailResponse> convertToResponse(List<PaymentDetail> paymentDetails) {
        return paymentDetails.stream()
                .map(pd -> ResidentPaymentDetailResponse.builder()
                        .paymentDetailId(pd.getPaymentDetailId())
                        .periodInfo(pd.getPaymentPeriod().getMonth() + "/" + pd.getPaymentPeriod().getYear())
                        .serviceTypeName(pd.getServiceType().getServiceName())
                        .amount(pd.getAmount())
                        .price(pd.getPrice())
                        .status(pd.getStatus())
                        .transactionCode(pd.getTransactionCode())
                        .createdAt(pd.getCreatedAt())
                        .paidAt(pd.getPaidAt())
                        .apartmentCode(pd.getOwnership().getApartment().getApartmentCode())
                        .ownerName(pd.getOwnership().getUser().getFullName())
                        .note(pd.getNote())
                        .build())
                .collect(Collectors.toList());
    }

    @Transactional
    public ResidentPaymentDetailResponse updatePaymentStatus(PaymentStatusUpdateRequest request) {
        PaymentDetail paymentDetail = paymentDetailRepository.findById(request.getPaymentDetailId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy chi tiết thanh toán"));

        paymentDetail.setStatus(request.getStatus());
        paymentDetail.setNote(request.getNote());

        if (request.getStatus() == PaymentDetail.Status.PAID) {
            paymentDetail.setPaidAt(LocalDateTime.now());
        }

        paymentDetail = paymentDetailRepository.save(paymentDetail);
        return convertToResponse(List.of(paymentDetail)).get(0);
    }
} 