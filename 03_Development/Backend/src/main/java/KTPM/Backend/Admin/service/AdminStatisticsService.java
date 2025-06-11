package KTPM.Backend.Admin.service;

import KTPM.Backend.Admin.dto.ResidentStatisticsDTO;
import java.util.Map;

public interface AdminStatisticsService {
    ResidentStatisticsDTO getResidentStats();
    Map<String, Long> getApartmentStats();
    Map<String, Long> getVehicleStats();
}