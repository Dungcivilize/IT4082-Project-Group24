package KTPM.Backend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import KTPM.Backend.entity.PaymentPeriod;
import KTPM.Backend.entity.PaymentDetail;
import KTPM.Backend.repository.PaymentPeriodRepository;
import KTPM.Backend.repository.PaymentDetailRepository;
import KTPM.Backend.dto.PaymentPeriodDTO;

@Service
public class PaymentPeriodService {
    @Autowired
    private PaymentPeriodRepository paymentPeriodRepository;

    @Autowired
    private PaymentDetailRepository paymentDetailRepository;

    public List<PaymentPeriod> getAllPaymentPeriods() {
        return paymentPeriodRepository.findAll();
    }

    public PaymentPeriod getPaymentPeriodById(Integer id) {
        return paymentPeriodRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy kỳ thu phí"));
    }

    public List<PaymentPeriodDTO> getAllPaymentPeriodsWithStatus() {
        List<PaymentPeriod> periods = paymentPeriodRepository.findAll();
        return periods.stream()
                .map(period -> {
                    boolean isCompleted = isPaymentPeriodCompleted(period.getPaymentPeriodId());
                    return new PaymentPeriodDTO(
                        period.getPaymentPeriodId(),
                        period.getMonth(),
                        period.getYear(),
                        isCompleted
                    );
                })
                .toList();
    }

    public boolean isPaymentPeriodCompleted(Integer periodId) {
        List<PaymentDetail> details = paymentDetailRepository.findByPaymentPeriodPaymentPeriodId(periodId);
        if (details.isEmpty()) {
            return false;
        }
        return details.stream().allMatch(detail -> detail.getStatus() == PaymentDetail.Status.PAID);
    }

    public PaymentPeriod createPaymentPeriod(PaymentPeriod paymentPeriod) {
        // Check if payment period already exists
        List<PaymentPeriod> existingPeriods = paymentPeriodRepository.findAll();
        boolean exists = existingPeriods.stream()
                .anyMatch(p -> p.getMonth().equals(paymentPeriod.getMonth()) 
                        && p.getYear().equals(paymentPeriod.getYear()));
        
        if (exists) {
            throw new RuntimeException("Kỳ thu phí này đã tồn tại");
        }

        // Mặc định trạng thái là collecting khi tạo mới
        paymentPeriod.setStatus(PaymentPeriod.Status.collecting);
        return paymentPeriodRepository.save(paymentPeriod);
    }
} 