package com.KTPM.KTPM.Controllers;

import com.KTPM.KTPM.DTO.CollectionPeriodDTO;
import com.KTPM.KTPM.Services.CollectionPeriodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/collection-periods")
@CrossOrigin(origins = "http://localhost:5173") // sửa lại nếu FE dùng cổng khác
public class CollectionPeriodController {

    @Autowired
    private CollectionPeriodService collectionPeriodService;

    @GetMapping
    public List<CollectionPeriodDTO> getAllCollectionPeriods() {
        return collectionPeriodService.getAllCollectionPeriods();
    }

}
