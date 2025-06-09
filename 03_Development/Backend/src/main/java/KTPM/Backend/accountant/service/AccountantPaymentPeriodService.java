package KTPM.Backend.accountant.service;

import KTPM.Backend.accountant.dto.AccountantPaymentPeriodResponse;
import KTPM.Backend.entity.*;
import KTPM.Backend.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccountantPaymentPeriodService {

    private final PaymentPeriodRepository paymentPeriodRepository;
    private final PaymentDetailRepository paymentDetailRepository;
    private final ApartmentRepository apartmentRepository;
    private final ServiceTypeRepository serviceTypeRepository;
    private final VehicleRepository vehicleRepository;

    public AccountantPaymentPeriodService(PaymentPeriodRepository paymentPeriodRepository,
                                          PaymentDetailRepository paymentDetailRepository,
                                          ApartmentRepository apartmentRepository,
                                          ServiceTypeRepository serviceTypeRepository,
                                          VehicleRepository vehicleRepository) {
        this.paymentPeriodRepository = paymentPeriodRepository;
        this.paymentDetailRepository = paymentDetailRepository;
        this.apartmentRepository = apartmentRepository;
        this.serviceTypeRepository = serviceTypeRepository;
        this.vehicleRepository = vehicleRepository;
    }

    // Lấy danh sách đợt thu phí cơ bản (không kèm chi tiết)
    public List<AccountantPaymentPeriodResponse> getAllPaymentPeriods() {
        List<PaymentPeriod> periods = paymentPeriodRepository.findAll();

        return periods.stream().map(pp -> {
            AccountantPaymentPeriodResponse dto = new AccountantPaymentPeriodResponse();
            dto.setPaymentPeriodId(pp.getPaymentPeriodId());
            dto.setMonth(pp.getMonth());
            dto.setYear(pp.getYear());
            dto.setStatus(pp.getStatus().name());
            dto.setNote(pp.getNote());
            dto.setApartments(null);
            return dto;
        }).collect(Collectors.toList());
    }

    // Lấy chi tiết căn hộ + payment detail theo paymentPeriodId
    public AccountantPaymentPeriodResponse getPaymentPeriodDetail(Integer paymentPeriodId) {
        PaymentPeriod pp = paymentPeriodRepository.findById(paymentPeriodId)
                .orElseThrow(() -> new RuntimeException("Payment period not found"));

        AccountantPaymentPeriodResponse dto = new AccountantPaymentPeriodResponse();
        dto.setPaymentPeriodId(pp.getPaymentPeriodId());
        dto.setMonth(pp.getMonth());
        dto.setYear(pp.getYear());
        dto.setStatus(pp.getStatus().name());
        dto.setNote(pp.getNote());

        List<PaymentDetail> paymentDetails = paymentDetailRepository.findByPaymentPeriod_PaymentPeriodId(paymentPeriodId);

        var apartmentMap = paymentDetails.stream()
                .collect(Collectors.groupingBy(PaymentDetail::getApartment));

        List<AccountantPaymentPeriodResponse.ApartmentPaymentDetailDto> apartmentDtos = apartmentMap.entrySet().stream()
                .map(entry -> {
                    Apartment apartment = entry.getKey();
                    List<PaymentDetail> pds = entry.getValue();

                    List<AccountantPaymentPeriodResponse.PaymentDetailDto> paymentDetailDtos = pds.stream()
                            .map(pd -> {
                                ServiceType st = pd.getServiceType();
                                Integer quantity = null;
                                if (st.getServiceType() == ServiceType.ServiceTypeEnum.motorbike) {
                                    quantity = vehicleRepository.countByApartmentAndType(apartment, Vehicle.VehicleType.motorcycle);
                                } else if (st.getServiceType() == ServiceType.ServiceTypeEnum.car) {
                                    quantity = vehicleRepository.countByApartmentAndType(apartment, Vehicle.VehicleType.car);
                                }
                                double amount = (quantity != null) ? quantity * st.getUnitPrice().doubleValue() : pd.getAmount().doubleValue();

                                return new AccountantPaymentPeriodResponse.PaymentDetailDto(
                                        pd.getPaymentDetailId(),
                                        st.getServiceName(),
                                        st.getServiceType().name(),
                                        amount,
                                        pd.getStatus().name(),
                                        pd.getCreatedAt().toString(),
                                        quantity,
                                        st.getUnitPrice().doubleValue()
                                );
                            }).collect(Collectors.toList());

                    return new AccountantPaymentPeriodResponse.ApartmentPaymentDetailDto(
                            apartment.getApartmentId(),
                            apartment.getApartmentCode(),
                            paymentDetailDtos
                    );
                }).collect(Collectors.toList());

        dto.setApartments(apartmentDtos);
        return dto;
    }

    // Tạo đợt thu phí mới kèm tạo payment detail cho từng căn hộ và từng dịch vụ
    @Transactional
    public PaymentPeriod createPaymentPeriodWithDetails(int month, int year, String note) {
        // 1. Tạo đợt thu phí mới
        PaymentPeriod paymentPeriod = new PaymentPeriod();
        paymentPeriod.setMonth(month);
        paymentPeriod.setYear(year);
        paymentPeriod.setNote(note);
        paymentPeriod.setStatus(PaymentPeriod.PaymentPeriodStatus.collecting);
        // trạng thái mặc định
        paymentPeriod = paymentPeriodRepository.save(paymentPeriod);

        // 2. Lấy danh sách tất cả căn hộ
        List<Apartment> apartments = apartmentRepository.findAll();

        // 3. Lấy tất cả các loại dịch vụ cần tạo hóa đơn (ví dụ 5 loại)
        ServiceType electricity = serviceTypeRepository.findByServiceType(ServiceType.ServiceTypeEnum.electricity)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy service electricity"));
        ServiceType water = serviceTypeRepository.findByServiceType(ServiceType.ServiceTypeEnum.water)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy service water"));
        ServiceType motorbike = serviceTypeRepository.findByServiceType(ServiceType.ServiceTypeEnum.motorbike)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy service motorbike"));
        ServiceType car = serviceTypeRepository.findByServiceType(ServiceType.ServiceTypeEnum.car)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy service car"));
        ServiceType management = serviceTypeRepository.findByServiceType(ServiceType.ServiceTypeEnum.management)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy service management"));

        // 4. Tạo PaymentDetail cho từng căn hộ và từng serviceType
        for (Apartment apartment : apartments) {
            // Điện
            PaymentDetail electricityDetail = new PaymentDetail();
            electricityDetail.setApartment(apartment);
            electricityDetail.setPaymentPeriod(paymentPeriod);
            electricityDetail.setServiceType(electricity);
            electricityDetail.setAmount(BigDecimal.ZERO); // Lưu lượng điện chưa nhập nên amount=0
            paymentDetailRepository.save(electricityDetail);

            // Nước
            PaymentDetail waterDetail = new PaymentDetail();
            waterDetail.setApartment(apartment);
            waterDetail.setPaymentPeriod(paymentPeriod);
            waterDetail.setServiceType(water);
            waterDetail.setAmount(BigDecimal.ZERO); // Lưu lượng nước chưa nhập nên amount=0
            paymentDetailRepository.save(waterDetail);

            // Xe máy
            int motorbikeCount = vehicleRepository.countByApartmentAndType(apartment, Vehicle.VehicleType.motorcycle);
            BigDecimal motorbikeAmount = motorbike.getUnitPrice().multiply(BigDecimal.valueOf(motorbikeCount));
            PaymentDetail motorbikeDetail = new PaymentDetail();
            motorbikeDetail.setApartment(apartment);
            motorbikeDetail.setPaymentPeriod(paymentPeriod);
            motorbikeDetail.setServiceType(motorbike);
            motorbikeDetail.setAmount(motorbikeAmount);
            paymentDetailRepository.save(motorbikeDetail);

            // Ô tô
            int carCount = vehicleRepository.countByApartmentAndType(apartment, Vehicle.VehicleType.car);
            BigDecimal carAmount = car.getUnitPrice().multiply(BigDecimal.valueOf(carCount));
            PaymentDetail carDetail = new PaymentDetail();
            carDetail.setApartment(apartment);
            carDetail.setPaymentPeriod(paymentPeriod);
            carDetail.setServiceType(car);
            carDetail.setAmount(carAmount);
            paymentDetailRepository.save(carDetail);

            // Phí quản lý
            PaymentDetail managementDetail = new PaymentDetail();
            managementDetail.setApartment(apartment);
            managementDetail.setPaymentPeriod(paymentPeriod);
            managementDetail.setServiceType(management);
            managementDetail.setAmount(management.getUnitPrice()); // Lấy trực tiếp đơn giá
            paymentDetailRepository.save(managementDetail);
        }

        return paymentPeriod;
    }
}
