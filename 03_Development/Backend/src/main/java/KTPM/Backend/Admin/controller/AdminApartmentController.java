package KTPM.Backend.Admin.controller;

import KTPM.Backend.Admin.dto.AdminApartmentDTO;
import KTPM.Backend.Admin.service.AdminApartmentService;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.List;


@RestController
@RequestMapping("/api/admin/apartments")
public class AdminApartmentController {

    @Autowired
    private AdminApartmentService apartmentService;

    @GetMapping
    public ResponseEntity<List<AdminApartmentDTO>> getAllApartments() {
        return ResponseEntity.ok(apartmentService.getAllApartments());
    }

    @GetMapping("/empty")
    public ResponseEntity<List<AdminApartmentDTO>> getEmptyApartments() {
        return ResponseEntity.ok(apartmentService.getEmptyApartments());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AdminApartmentDTO> getApartmentById(@PathVariable Integer id) {
        return apartmentService.getApartmentById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<AdminApartmentDTO> createApartment(@RequestBody AdminApartmentDTO dto) {
        return ResponseEntity.ok(apartmentService.createApartment(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AdminApartmentDTO> updateApartment(
            @PathVariable Integer id, 
            @RequestBody AdminApartmentDTO dto) {
        return apartmentService.updateApartment(id, dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteApartment(@PathVariable Integer id) {
        if (apartmentService.deleteApartment(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}