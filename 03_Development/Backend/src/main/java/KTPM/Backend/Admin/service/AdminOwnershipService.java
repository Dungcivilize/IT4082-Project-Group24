package KTPM.Backend.Admin.service;

import KTPM.Backend.Admin.dto.AdminOwnershipDTO;
import KTPM.Backend.entity.ApartmentOwnership;
import KTPM.Backend.entity.PaymentDetail;
import KTPM.Backend.repository.ApartmentOwnershipRepository;
import KTPM.Backend.repository.PaymentDetailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminOwnershipService {
    private final ApartmentOwnershipRepository ownershipRepository;
    private final PaymentDetailRepository paymentDetailRepository;

    public List<AdminOwnershipDTO> getActiveOwnerships() {
        return ownershipRepository.findActiveOwnershipDTOs();
    }

    public AdminOwnershipDTO updateOwnershipStatus(Integer id, AdminOwnershipDTO updatedOwnership) {
        ApartmentOwnership ownership = ownershipRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ownership not found"));

        // Cập nhật status
        if (updatedOwnership.getOwnershipStatus() != null) {
            // Kiểm tra nếu chuyển sang inactive thì phải thanh toán hết hóa đơn
            if (updatedOwnership.getOwnershipStatus() == ApartmentOwnership.Status.inactive) {
                List<PaymentDetail> unpaidPayments = paymentDetailRepository.findByStatusAndOwnershipOwnershipId(
                        PaymentDetail.Status.UNPAID,ownership.getOwnershipId());

                if (!unpaidPayments.isEmpty()) {
                    throw new RuntimeException("Không thể kết thúc hợp đồng khi còn hóa đơn chưa thanh toán");
                }
            }

            ownership.setStatus(updatedOwnership.getOwnershipStatus());

            // Nếu chuyển thành inactive → set endDate
            if (updatedOwnership.getOwnershipStatus() == ApartmentOwnership.Status.inactive) {
                ownership.setEndDate(LocalDate.now());
            }
        }

        ApartmentOwnership saved = ownershipRepository.save(ownership);

        return new AdminOwnershipDTO(
                saved.getOwnershipId(),
                saved.getUser().getFullName(),
                saved.getApartment().getApartmentId(),
                saved.getApartment().getApartmentCode(),
                saved.getApartment().getFloor(),
                saved.getStatus()
        );
    }
}
