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
public class StatisticsByPeriodResponse {
    private BigDecimal totalAmount;
    private BigDecimal collectedAmount;
    private BigDecimal uncollectedAmount;
    private List<ServiceDetail> serviceDetails;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ServiceDetail {
        private Integer serviceTypeId;
        private String serviceName;
        private BigDecimal totalAmount;
        private BigDecimal collectedAmount;
        private BigDecimal uncollectedAmount;
    }
} 