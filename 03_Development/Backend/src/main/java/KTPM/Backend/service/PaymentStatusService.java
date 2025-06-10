package KTPM.Backend.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import KTPM.Backend.dto.PaymentStatusUpdateRequest;
import KTPM.Backend.dto.PaymentDetailUpdateRequest;
import KTPM.Backend.dto.ResidentPaymentDetailResponse;
import KTPM.Backend.entity.PaymentDetail;
import KTPM.Backend.entity.PaymentDetail.Status;
import KTPM.Backend.entity.ApartmentOwnership;
import KTPM.Backend.repository.PaymentDetailRepository;
import KTPM.Backend.repository.ApartmentOwnershipRepository;
import KTPM.Backend.exception.EntityNotFoundException;

@Service
public class PaymentStatusService {
    @Autowired
    private PaymentDetailRepository paymentDetailRepository;

    @Autowired
    private ApartmentOwnershipRepository apartmentOwnershipRepository;

    // Phương thức cho cư dân - chỉ xem được hóa đơn của ownership_id của họ
    public List<ResidentPaymentDetailResponse> getResidentPayments(Integer ownershipId) {
        List<PaymentDetail> payments = paymentDetailRepository.findByOwnershipOwnershipId(ownershipId);
        return payments.stream()
                .map(ResidentPaymentDetailResponse::fromEntity)
                .collect(Collectors.toList());
    }

    // Phương thức cho cư dân - xem hóa đơn theo trạng thái
    public List<ResidentPaymentDetailResponse> getResidentPaymentsByStatus(Integer ownershipId, Status status) {
        List<PaymentDetail> payments = paymentDetailRepository.findByStatusAndOwnershipOwnershipId(status, ownershipId);
        return payments.stream()
                .map(ResidentPaymentDetailResponse::fromEntity)
                .collect(Collectors.toList());
    }

    // Phương thức cho kế toán - xem tất cả hóa đơn theo nhiều tiêu chí
    public List<ResidentPaymentDetailResponse> getAccountantPayments(Integer paymentPeriodId, String ownershipIds) {
        List<PaymentDetail> payments;
        
        if (paymentPeriodId != null && ownershipIds != null) {
            List<Integer> ownershipIdList = Arrays.stream(ownershipIds.split(","))
                .map(Integer::parseInt)
                .collect(Collectors.toList());
            List<ApartmentOwnership> ownerships = ownershipIdList.stream()
                .map(id -> apartmentOwnershipRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy quyền sở hữu")))
                .collect(Collectors.toList());
            payments = paymentDetailRepository.findByOwnershipInAndPaymentPeriodPaymentPeriodId(ownerships, paymentPeriodId);
        } else if (paymentPeriodId != null) {
            payments = paymentDetailRepository.findByPaymentPeriodPaymentPeriodId(paymentPeriodId);
        } else if (ownershipIds != null) {
            List<Integer> ownershipIdList = Arrays.stream(ownershipIds.split(","))
                .map(Integer::parseInt)
                .collect(Collectors.toList());
            List<ApartmentOwnership> ownerships = ownershipIdList.stream()
                .map(id -> apartmentOwnershipRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy quyền sở hữu")))
                .collect(Collectors.toList());
            payments = paymentDetailRepository.findByOwnershipIn(ownerships);
        } else {
            payments = paymentDetailRepository.findAll();
        }
        return payments.stream()
                .map(ResidentPaymentDetailResponse::fromEntity)
                .collect(Collectors.toList());
    }

    // Phương thức cho kế toán - xem hóa đơn theo trạng thái
    public List<ResidentPaymentDetailResponse> getAccountantPaymentsByStatus(Status status, Integer paymentPeriodId, String ownershipIds) {
        List<PaymentDetail> payments;
        
        if (paymentPeriodId != null && ownershipIds != null) {
            List<Integer> ownershipIdList = Arrays.stream(ownershipIds.split(","))
                .map(Integer::parseInt)
                .collect(Collectors.toList());
            List<ApartmentOwnership> ownerships = ownershipIdList.stream()
                .map(id -> apartmentOwnershipRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy quyền sở hữu")))
                .collect(Collectors.toList());
            payments = paymentDetailRepository.findByStatusAndOwnershipInAndPaymentPeriodPaymentPeriodId(status, ownerships, paymentPeriodId);
        } else if (paymentPeriodId != null) {
            payments = paymentDetailRepository.findByStatusAndPaymentPeriodPaymentPeriodId(status, paymentPeriodId);
        } else if (ownershipIds != null) {
            List<Integer> ownershipIdList = Arrays.stream(ownershipIds.split(","))
                .map(Integer::parseInt)
                .collect(Collectors.toList());
            List<ApartmentOwnership> ownerships = ownershipIdList.stream()
                .map(id -> apartmentOwnershipRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy quyền sở hữu")))
                .collect(Collectors.toList());
            payments = paymentDetailRepository.findByStatusAndOwnershipIn(status, ownerships);
        } else {
            payments = paymentDetailRepository.findByStatus(status);
        }
        return payments.stream()
                .map(ResidentPaymentDetailResponse::fromEntity)
                .collect(Collectors.toList());
    }

    // Phương thức cho kế toán - cập nhật trạng thái thanh toán
    @Transactional
    public ResidentPaymentDetailResponse updatePaymentStatus(PaymentStatusUpdateRequest request) {
        PaymentDetail payment = paymentDetailRepository.findById(request.getPaymentDetailId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy chi tiết thanh toán"));

        payment.setStatus(request.getStatus());
        payment.setNote(request.getNote());
        
        if (request.getStatus() == Status.PAID) {
            payment.setPaidAt(LocalDateTime.now());
        }

        payment = paymentDetailRepository.save(payment);
        return ResidentPaymentDetailResponse.fromEntity(payment);
    }

    public ResidentPaymentDetailResponse updatePaymentDetail(PaymentDetailUpdateRequest request) {
        PaymentDetail paymentDetail = paymentDetailRepository.findById(request.getPaymentDetailId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy hóa đơn"));

        if (paymentDetail.getStatus() != PaymentDetail.Status.UNPAID) {
            throw new RuntimeException("Chỉ có thể cập nhật hóa đơn chưa thanh toán");
        }

        paymentDetail.setAmount(request.getAmount());
        paymentDetail.setNote(request.getNote());

        PaymentDetail updatedPaymentDetail = paymentDetailRepository.save(paymentDetail);
        return ResidentPaymentDetailResponse.fromEntity(updatedPaymentDetail);
    }
} 