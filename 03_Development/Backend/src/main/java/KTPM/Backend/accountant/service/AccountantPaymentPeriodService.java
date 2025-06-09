package KTPM.Backend.accountant.service;


import KTPM.Backend.accountant.dto.*;
import KTPM.Backend.entity.*;
import KTPM.Backend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;
import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class AccountantPaymentPeriodService {

    private final PaymentPeriodRepository paymentPeriodRepository;
    private final ApartmentRepository apartmentRepository;
    private final PaymentDetailRepository paymentDetailRepository;
    private final ServiceTypeRepository serviceTypeRepository;
    private final VehicleRepository vehicleRepository;

    public PaymentPeriodWithApartmentsDTO getPaymentPeriodDetails(Long paymentPeriodId) {
        PaymentPeriod period = paymentPeriodRepository.findById(paymentPeriodId)
                .orElseThrow(() -> new RuntimeException("Payment period not found"));

        List<Apartment> apartments = apartmentRepository.findByStatus(Apartment.ApartmentStatus.occupied);

        List<PaymentDetail> allDetails = paymentDetailRepository
                .findByPaymentPeriod_PaymentPeriodId(paymentPeriodId.intValue());

        Map<Integer, List<PaymentDetail>> detailsByApartment = allDetails.stream()
                .collect(Collectors.groupingBy(pd -> pd.getApartment().getApartmentId()));

        List<ApartmentWithInvoicesDTO> apartmentDTOs = new ArrayList<>();

        for (Apartment apartment : apartments) {
            List<PaymentDetail> details = detailsByApartment.getOrDefault(apartment.getApartmentId(), Collections.emptyList());

            List<PaymentDetailDTO> invoiceDTOs = details.stream().map(pd -> {
                ServiceType st = pd.getServiceType();
                PaymentDetailDTO dto = new PaymentDetailDTO();
                dto.setPaymentDetailId(pd.getPaymentDetailId());
                dto.setServiceName(st.getServiceName());
                dto.setServiceType(st.getServiceType().toString());
                dto.setAmount(pd.getAmount());
                dto.setUnitPrice(st.getUnitPrice());
                dto.setStatus(pd.getStatus().toString());
                return dto;
            }).collect(Collectors.toList());

            ApartmentWithInvoicesDTO apartmentDTO = new ApartmentWithInvoicesDTO();
            apartmentDTO.setApartmentId(apartment.getApartmentId());
            apartmentDTO.setApartmentCode(apartment.getApartmentCode());
            apartmentDTO.setInvoices(invoiceDTOs);

            apartmentDTOs.add(apartmentDTO);
        }

        PaymentPeriodWithApartmentsDTO result = new PaymentPeriodWithApartmentsDTO();
        result.setPaymentPeriodId(period.getPaymentPeriodId());
        result.setMonth(period.getMonth());
        result.setYear(period.getYear());
        result.setApartments(apartmentDTOs);

        return result;
    }

    public List<PaymentPeriodBasicDTO> getAllPaymentPeriods() {
        List<PaymentPeriod> entities = paymentPeriodRepository.findAll();
        return entities.stream()
                .map(e -> new PaymentPeriodBasicDTO(e.getPaymentPeriodId(), e.getMonth(), e.getYear()))
                .collect(Collectors.toList());
    }

    @Transactional
    public PaymentPeriod createPaymentPeriodWithDetails(Integer month, Integer year, String note) {
        PaymentPeriod period = new PaymentPeriod();
        period.setMonth(month);
        period.setYear(year);
        period.setNote(note);
        period = paymentPeriodRepository.save(period);

        List<Apartment> apartments = apartmentRepository.findByStatus(Apartment.ApartmentStatus.occupied);
        List<ServiceType> serviceTypes = serviceTypeRepository.findAll();

        List<PaymentDetail> paymentDetailsToSave = new ArrayList<>();

        for (Apartment apartment : apartments) {
            long motorbikeCount = vehicleRepository.countByApartment_ApartmentIdAndType(apartment.getApartmentId(), Vehicle.VehicleType.motorcycle);
            long carCount = vehicleRepository.countByApartment_ApartmentIdAndType(apartment.getApartmentId(), Vehicle.VehicleType.car);

            for (ServiceType serviceType : serviceTypes) {
                PaymentDetail detail = new PaymentDetail();
                detail.setApartment(apartment);
                detail.setPaymentPeriod(period);
                detail.setServiceType(serviceType);
                detail.setStatus(PaymentDetail.PaymentDetailStatus.pending);

                String typeStr = serviceType.getServiceType().toString();

                if (typeStr.equalsIgnoreCase("motorbike")) {
                    if (motorbikeCount == 0) continue; // bỏ qua không tạo hóa đơn
                    detail.setAmount(BigDecimal.valueOf(motorbikeCount));
                } else if (typeStr.equalsIgnoreCase("car")) {
                    if (carCount == 0) continue; // bỏ qua không tạo hóa đơn
                    detail.setAmount(BigDecimal.valueOf(carCount));
                } else {
                    detail.setAmount(BigDecimal.ZERO);
                }
                paymentDetailsToSave.add(detail);
            }
        }

        paymentDetailRepository.saveAll(paymentDetailsToSave);

        return period;
    }

    public PaymentDetailDTO updatePaymentDetail(Integer id, UpdatePaymentDetailRequestDTO updateRequest) {
        Optional<PaymentDetail> optionalDetail = paymentDetailRepository.findById(id);
        if (optionalDetail.isEmpty()) {
            return null;  // Không tìm thấy detail
        }

        PaymentDetail detail = optionalDetail.get();

        // Chỉ cập nhật amount nếu serviceType không phải motorbike hoặc car
        String serviceType = detail.getServiceType().getServiceType().toString().toLowerCase();
        if (!serviceType.equals("motorbike") && !serviceType.equals("car")) {
            BigDecimal newAmount = updateRequest.getAmount();
            if (newAmount != null && newAmount.compareTo(BigDecimal.ZERO) >= 0) {
                detail.setAmount(newAmount);
            }
        }

        paymentDetailRepository.save(detail);

        // Chuyển đổi sang DTO trả về
        PaymentDetailDTO dto = new PaymentDetailDTO();
        dto.setPaymentDetailId(detail.getPaymentDetailId());
        dto.setServiceName(detail.getServiceType().getServiceName());
        dto.setServiceType(detail.getServiceType().getServiceType().toString());
        dto.setAmount(detail.getAmount());
        dto.setUnitPrice(detail.getServiceType().getUnitPrice());
        dto.setStatus(detail.getStatus().toString());

        return dto;
    }

}
