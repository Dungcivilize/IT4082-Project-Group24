package KTPM.Backend.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateOwnershipRequest {
    @NotNull(message = "Mã căn hộ không được để trống")
    private Integer apartmentId;

    @NotNull(message = "Mã người dùng không được để trống")
    private Integer userId;

    @NotNull(message = "Ngày bắt đầu không được để trống")
    private LocalDate startDate;
} 