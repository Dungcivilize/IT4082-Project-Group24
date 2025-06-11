package KTPM.Backend.Admin.controller;

import KTPM.Backend.Admin.dto.ResidentStatisticsDTO;
import KTPM.Backend.Admin.service.AdminStatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/statistics")
public class AdminStatisticsController {

    @Autowired
    private AdminStatisticsService statisticsService;

    // Thống kê dân cư: giới tính, độ tuổi, trạng thái
    @GetMapping("/residents")
    public ResponseEntity<ResidentStatisticsDTO> getResidentStats() {
        return ResponseEntity.ok(statisticsService.getResidentStats());
    }

    // Thống kê căn hộ: đang ở/trống
    @GetMapping("/apartments")
    public ResponseEntity<Map<String, Long>> getApartmentStats() {
        return ResponseEntity.ok(statisticsService.getApartmentStats());
    }

    // Thống kê phương tiện: theo loại
    @GetMapping("/vehicles")
    public ResponseEntity<Map<String, Long>> getVehicleStats() {
        return ResponseEntity.ok(statisticsService.getVehicleStats());
    }
}
