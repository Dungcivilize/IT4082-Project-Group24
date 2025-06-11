package KTPM.Backend.Admin.dto;

import lombok.Data;

import java.util.Map;

@Data
public class ResidentStatisticsDTO {
    private Map<String, Long> genderStats;
    private Map<String, Long> ageStats;
    private Map<String, Long> statusStats;

    public ResidentStatisticsDTO(Map<String, Long> genderStats, Map<String, Long> ageStats, Map<String, Long> statusStats) {
        this.genderStats = genderStats;
        this.ageStats = ageStats;
        this.statusStats = statusStats;
    }
}
