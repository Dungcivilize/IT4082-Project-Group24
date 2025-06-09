package KTPM.Backend.dto;

import java.math.BigDecimal;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StatisticsByServiceResponse {
    private BigDecimal totalAmount;
    private BigDecimal collectedAmount;
    private BigDecimal uncollectedAmount;
    private List<PeriodDetail> periodDetails;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PeriodDetail {
        private Integer paymentPeriodId;
        private Integer month;
        private Integer year;
        private BigDecimal totalAmount;
        private BigDecimal collectedAmount;
        private BigDecimal uncollectedAmount;
    }
} 