package com.KTPM.KTPM.Controllers;

import com.KTPM.KTPM.DTO.CollectionSummaryByNameDTO;
import com.KTPM.KTPM.Services.CollectionSummaryService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/collection-summary")
@CrossOrigin(origins = "http://localhost:5173")
public class CollectionSummaryController {

    private final CollectionSummaryService collectionSummaryService;

    public CollectionSummaryController(CollectionSummaryService collectionSummaryService) {
        this.collectionSummaryService = collectionSummaryService;
    }

    @GetMapping
    public List<CollectionSummaryByNameDTO> getCollectionSummary() {
        return collectionSummaryService.getCollectionSummaryByName();
    }
}
