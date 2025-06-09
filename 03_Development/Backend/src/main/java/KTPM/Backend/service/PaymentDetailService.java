package KTPM.Backend.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import KTPM.Backend.entity.ApartmentOwnership;
import KTPM.Backend.entity.PaymentDetail;
import KTPM.Backend.entity.PaymentPeriod;
import KTPM.Backend.entity.ServiceType;
import KTPM.Backend.repository.PaymentDetailRepository;
import KTPM.Backend.repository.PaymentPeriodRepository;
import KTPM.Backend.repository.ServiceTypeRepository;

@Service
public class PaymentDetailService {
    @Autowired
    private PaymentDetailRepository paymentDetailRepository;

    @Autowired
    private PaymentPeriodRepository paymentPeriodRepository;

    @Autowired
    private ServiceTypeRepository serviceTypeRepository;

    @Autowired
    private ApartmentOwnershipService apartmentOwnershipService;

    public List<PaymentDetail> getAllPaymentDetails() {
        return paymentDetailRepository.findAll();
    }

    public PaymentDetail getPaymentDetailById(Integer id) {
        return paymentDetailRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy chi tiết thanh toán"));
    }

    public List<PaymentDetail> getPaymentDetailsByPeriod(Integer periodId) {
        return paymentDetailRepository.findByPaymentPeriodPaymentPeriodId(periodId);
    }

    public List<PaymentDetail> getPaymentDetailsByOwnership(Integer ownershipId) {
        return paymentDetailRepository.findByOwnershipOwnershipId(ownershipId);
    }

    @Transactional
    public PaymentDetail createPaymentDetail(Integer periodId, Integer ownershipId, Integer serviceTypeId, Double amount) {
        // Kiểm tra kỳ thu phí có tồn tại không
        PaymentPeriod period = paymentPeriodRepository.findById(periodId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy kỳ thu phí"));

        // Kiểm tra quyền sở hữu có tồn tại không
        ApartmentOwnership ownership = apartmentOwnershipService.getOwnershipById(ownershipId);

        // Kiểm tra loại dịch vụ có tồn tại không
        ServiceType serviceType = serviceTypeRepository.findById(serviceTypeId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy loại dịch vụ"));

        // Kiểm tra đã tồn tại chi tiết thanh toán cho kỳ này chưa
        paymentDetailRepository.findByPaymentPeriodAndOwnershipAndServiceType(period, ownership, serviceType)
                .ifPresent(detail -> {
                    throw new RuntimeException("Đã tồn tại chi tiết thanh toán cho kỳ này");
                });

        // Tạo chi tiết thanh toán mới
        PaymentDetail paymentDetail = new PaymentDetail();
        paymentDetail.setPaymentPeriod(period);
        paymentDetail.setOwnership(ownership);
        paymentDetail.setServiceType(serviceType);
        paymentDetail.setAmount(BigDecimal.valueOf(amount));
        paymentDetail.setStatus(PaymentDetail.Status.UNPAID);
        paymentDetail.setCreatedAt(LocalDateTime.now());

        return paymentDetailRepository.save(paymentDetail);
    }

    @Transactional
    public PaymentDetail updatePaymentDetail(Integer id, Double amount) {
        PaymentDetail paymentDetail = getPaymentDetailById(id);

        if (paymentDetail.getStatus() == PaymentDetail.Status.PAID) {
            throw new RuntimeException("Không thể cập nhật chi tiết thanh toán đã thanh toán");
        }

        paymentDetail.setAmount(BigDecimal.valueOf(amount));
        return paymentDetailRepository.save(paymentDetail);
    }

    @Transactional
    public void deletePaymentDetail(Integer id) {
        PaymentDetail paymentDetail = getPaymentDetailById(id);

        if (paymentDetail.getStatus() == PaymentDetail.Status.PAID) {
            throw new RuntimeException("Không thể xóa chi tiết thanh toán đã thanh toán");
        }

        paymentDetailRepository.delete(paymentDetail);
    }
} 