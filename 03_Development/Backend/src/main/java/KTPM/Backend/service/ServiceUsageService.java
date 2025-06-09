package KTPM.Backend.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import KTPM.Backend.dto.PaymentDetailRequest;
import KTPM.Backend.dto.ResidentPaymentDetailResponse;
import KTPM.Backend.entity.ApartmentOwnership;
import KTPM.Backend.entity.PaymentDetail;
import KTPM.Backend.entity.PaymentPeriod;
import KTPM.Backend.entity.ServiceType;
import KTPM.Backend.entity.Vehicle;
import KTPM.Backend.repository.ApartmentOwnershipRepository;
import KTPM.Backend.repository.PaymentDetailRepository;
import KTPM.Backend.repository.PaymentPeriodRepository;
import KTPM.Backend.repository.ServiceTypeRepository;
import KTPM.Backend.repository.VehicleRepository;
import jakarta.persistence.EntityNotFoundException;

@Service
public class ServiceUsageService {
    @Autowired
    private PaymentDetailRepository paymentDetailRepository;

    @Autowired
    private ServiceTypeRepository serviceTypeRepository;

    @Autowired
    private ApartmentOwnershipRepository apartmentOwnershipRepository;

    @Autowired
    private PaymentPeriodRepository paymentPeriodRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    public List<ResidentPaymentDetailResponse> getPaymentDetails(Integer paymentPeriodId, Integer ownershipId, PaymentDetail.Status status) {
        List<PaymentDetail> details;
        if (paymentPeriodId != null && ownershipId != null && status != null) {
            details = paymentDetailRepository.findByPaymentPeriodIdAndOwnershipIdAndStatus(paymentPeriodId, ownershipId, status);
        } else if (paymentPeriodId != null && ownershipId != null) {
            details = paymentDetailRepository.findByPaymentPeriodIdAndOwnershipId(paymentPeriodId, ownershipId);
        } else if (paymentPeriodId != null) {
            details = paymentDetailRepository.findByPaymentPeriodId(paymentPeriodId);
        } else if (ownershipId != null) {
            details = paymentDetailRepository.findByOwnershipId(ownershipId);
        } else {
            details = paymentDetailRepository.findAll();
        }
        return details.stream().map(this::convertToResponse).collect(Collectors.toList());
    }

    private ResidentPaymentDetailResponse convertToResponse(PaymentDetail pd) {
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
    }

    @Transactional
    public ResidentPaymentDetailResponse createPaymentDetail(PaymentDetailRequest request) {
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

        // Tính giá tiền
        paymentDetail.setPrice(paymentDetail.getAmount().multiply(serviceType.getUnitPrice()));

        paymentDetail = paymentDetailRepository.save(paymentDetail);
        return convertToResponse(paymentDetail);
    }
} 