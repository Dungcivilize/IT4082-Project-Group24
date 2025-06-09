package KTPM.Backend.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EndOwnershipRequest {
    @NotNull(message = "Ngày kết thúc không được để trống")
    private LocalDate endDate;
} 