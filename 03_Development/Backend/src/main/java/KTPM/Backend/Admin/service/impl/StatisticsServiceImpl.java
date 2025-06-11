package KTPM.Backend.Admin.service.impl;

import KTPM.Backend.Admin.dto.ResidentStatisticsDTO;
import KTPM.Backend.Admin.service.AdminStatisticsService;
import KTPM.Backend.entity.Apartment;
import KTPM.Backend.repository.ApartmentRepository;
import KTPM.Backend.repository.ResidentRepository;
import KTPM.Backend.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StatisticsServiceImpl implements AdminStatisticsService {

    @Autowired
    private ResidentRepository residentRepository;

    @Autowired
    private ApartmentRepository apartmentRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Override
    public ResidentStatisticsDTO getResidentStats() {
        Map<String, Long> genderStats = convertToMap(residentRepository.countByGenderRaw());
        Map<String, Long> statusStats = convertToMap(residentRepository.countByStatusRaw());

        Map<String, Long> ageStats = new HashMap<>();
        residentRepository.findAll().forEach(r -> {
            int age = Period.between(r.getBirthDate(), LocalDate.now()).getYears();
            if (age <= 17) ageStats.merge("0-17", 1L, Long::sum);
            else if (age <= 35) ageStats.merge("18-35", 1L, Long::sum);
            else if (age <= 60) ageStats.merge("36-60", 1L, Long::sum);
            else ageStats.merge("60+", 1L, Long::sum);
        });

        return new ResidentStatisticsDTO(genderStats, ageStats, statusStats);
    }

    @Override
public Map<String, Long> getApartmentStats() {
    Map<String, Long> result = new HashMap<>();
    result.put("occupied", apartmentRepository.countByStatus(Apartment.Status.occupied));
    result.put("empty", apartmentRepository.countByStatus(Apartment.Status.empty));
    return result;
}

    @Override
    public Map<String, Long> getVehicleStats() {
        return convertToMap(vehicleRepository.countByTypeRaw());
    }

    private Map<String, Long> convertToMap(List<Object[]> rawList) {
        Map<String, Long> map = new HashMap<>();
        for (Object[] obj : rawList) {
            String key = obj[0] != null ? obj[0].toString() : "unknown";
            Long value = ((Number) obj[1]).longValue();
            map.put(key, value);
        }
        return map;
    }
}
