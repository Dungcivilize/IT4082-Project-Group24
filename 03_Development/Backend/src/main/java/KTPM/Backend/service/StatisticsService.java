package KTPM.Backend.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import KTPM.Backend.dto.StatisticsByPeriodResponse;
import KTPM.Backend.dto.StatisticsByServiceResponse;
import KTPM.Backend.entity.PaymentDetail;
import KTPM.Backend.entity.PaymentPeriod;
import KTPM.Backend.entity.ServiceType;
import KTPM.Backend.repository.PaymentDetailRepository;
import KTPM.Backend.repository.PaymentPeriodRepository;
import KTPM.Backend.repository.ServiceTypeRepository;

@Service
public class StatisticsService {
    @Autowired
    private PaymentDetailRepository paymentDetailRepository;

    @Autowired
    private PaymentPeriodRepository paymentPeriodRepository;

    @Autowired
    private ServiceTypeRepository serviceTypeRepository;

    public StatisticsByPeriodResponse getStatisticsByPeriod(Integer periodId) {
        PaymentPeriod period = paymentPeriodRepository.findById(periodId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy kỳ thu phí"));

        List<PaymentDetail> payments = paymentDetailRepository.findByPaymentPeriodPaymentPeriodId(periodId);

        // Tính tổng số tiền
        BigDecimal totalAmount = payments.stream()
                .map(p -> p.getAmount().multiply(p.getServiceType().getUnitPrice()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Tính số tiền đã thu
        BigDecimal collectedAmount = payments.stream()
                .filter(p -> p.getStatus() == PaymentDetail.Status.PAID)
                .map(p -> p.getAmount().multiply(p.getServiceType().getUnitPrice()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Tính số tiền chưa thu
        BigDecimal uncollectedAmount = totalAmount.subtract(collectedAmount);

        // Thống kê theo từng loại dịch vụ
        Map<ServiceType, List<PaymentDetail>> paymentsByService = payments.stream()
                .collect(Collectors.groupingBy(PaymentDetail::getServiceType));

        List<StatisticsByPeriodResponse.ServiceDetail> serviceDetails = new ArrayList<>();

        paymentsByService.forEach((service, servicePayments) -> {
            BigDecimal serviceTotalAmount = servicePayments.stream()
                    .map(p -> p.getAmount().multiply(p.getServiceType().getUnitPrice()))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            BigDecimal serviceCollectedAmount = servicePayments.stream()
                    .filter(p -> p.getStatus() == PaymentDetail.Status.PAID)
                    .map(p -> p.getAmount().multiply(p.getServiceType().getUnitPrice()))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            serviceDetails.add(StatisticsByPeriodResponse.ServiceDetail.builder()
                    .serviceTypeId(service.getServiceTypeId())
                    .serviceName(service.getServiceName())
                    .totalAmount(serviceTotalAmount)
                    .collectedAmount(serviceCollectedAmount)
                    .uncollectedAmount(serviceTotalAmount.subtract(serviceCollectedAmount))
                    .build());
        });

        return StatisticsByPeriodResponse.builder()
                .totalAmount(totalAmount)
                .collectedAmount(collectedAmount)
                .uncollectedAmount(uncollectedAmount)
                .serviceDetails(serviceDetails)
                .build();
    }

    public StatisticsByServiceResponse getStatisticsByService(Integer serviceTypeId) {
        ServiceType serviceType = serviceTypeRepository.findById(serviceTypeId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy loại dịch vụ"));

        List<PaymentDetail> payments = paymentDetailRepository.findAll().stream()
                .filter(p -> p.getServiceType().getServiceTypeId().equals(serviceTypeId))
                .collect(Collectors.toList());

        // Tính tổng số tiền
        BigDecimal totalAmount = payments.stream()
                .map(p -> p.getAmount().multiply(p.getServiceType().getUnitPrice()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Tính số tiền đã thu
        BigDecimal collectedAmount = payments.stream()
                .filter(p -> p.getStatus() == PaymentDetail.Status.PAID)
                .map(p -> p.getAmount().multiply(p.getServiceType().getUnitPrice()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Tính số tiền chưa thu
        BigDecimal uncollectedAmount = totalAmount.subtract(collectedAmount);

        // Thống kê theo từng kỳ thu phí
        Map<PaymentPeriod, List<PaymentDetail>> paymentsByPeriod = payments.stream()
                .collect(Collectors.groupingBy(PaymentDetail::getPaymentPeriod));

        List<StatisticsByServiceResponse.PeriodDetail> periodDetails = new ArrayList<>();

        paymentsByPeriod.forEach((period, periodPayments) -> {
            BigDecimal periodTotalAmount = periodPayments.stream()
                    .map(p -> p.getAmount().multiply(p.getServiceType().getUnitPrice()))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            BigDecimal periodCollectedAmount = periodPayments.stream()
                    .filter(p -> p.getStatus() == PaymentDetail.Status.PAID)
                    .map(p -> p.getAmount().multiply(p.getServiceType().getUnitPrice()))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            periodDetails.add(StatisticsByServiceResponse.PeriodDetail.builder()
                    .paymentPeriodId(period.getPaymentPeriodId())
                    .month(period.getMonth())
                    .year(period.getYear())
                    .totalAmount(periodTotalAmount)
                    .collectedAmount(periodCollectedAmount)
                    .uncollectedAmount(periodTotalAmount.subtract(periodCollectedAmount))
                    .build());
        });

        return StatisticsByServiceResponse.builder()
                .totalAmount(totalAmount)
                .collectedAmount(collectedAmount)
                .uncollectedAmount(uncollectedAmount)
                .periodDetails(periodDetails)
                .build();
    }
} 