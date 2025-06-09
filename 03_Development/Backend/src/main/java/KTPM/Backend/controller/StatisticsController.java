package KTPM.Backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import KTPM.Backend.dto.StatisticsByPeriodResponse;
import KTPM.Backend.dto.StatisticsByServiceResponse;
import KTPM.Backend.service.StatisticsService;

@RestController
@RequestMapping("/api/statistics")
public class StatisticsController {
    @Autowired
    private StatisticsService statisticsService;

    @GetMapping("/by-period/{periodId}")
    public ResponseEntity<StatisticsByPeriodResponse> getStatisticsByPeriod(@PathVariable Integer periodId) {
        return ResponseEntity.ok(statisticsService.getStatisticsByPeriod(periodId));
    }

    @GetMapping("/by-service/{serviceTypeId}")
    public ResponseEntity<StatisticsByServiceResponse> getStatisticsByService(@PathVariable Integer serviceTypeId) {
        return ResponseEntity.ok(statisticsService.getStatisticsByService(serviceTypeId));
    }
} 