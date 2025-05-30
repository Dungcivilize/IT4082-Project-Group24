package com.KTPM.KTPM.Controllers;

import com.KTPM.KTPM.DTO.CreatePaymentRequest;
import com.KTPM.KTPM.DTO.PaymentDTO;
import com.KTPM.KTPM.Models.*;
import com.KTPM.KTPM.Models.Payment.PaymentStatus;
import com.KTPM.KTPM.Repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private CollectionPeriodRepository collectionPeriodRepository;
    @Autowired
    private CitizenRepository citizenRepository;
    @Autowired
    private ResidentRepository residentRepository;
    @Autowired
    private AccountantRepository accountantRepository;

    // Lấy các payment đã thanh toán
    @GetMapping("/citizen/{citizenId}/paid")
    public ResponseEntity<List<PaymentDTO>> getPaidPaymentsByCitizenId(@PathVariable Long citizenId) {
        List<Payment> payments = paymentRepository.findByCitizen_CitizenIdAndStatus(citizenId, PaymentStatus.PAID);
        List<PaymentDTO> paymentDTOs = payments.stream()
                .map(PaymentDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(paymentDTOs);
    }

    // Lấy các payment chưa thanh toán
    @GetMapping("/citizen/{citizenId}/unpaid")
    public ResponseEntity<List<PaymentDTO>> getUnpaidPaymentsByCitizenId(@PathVariable Long citizenId) {
        List<Payment> payments = paymentRepository.findByCitizen_CitizenIdAndStatus(citizenId, PaymentStatus.UNPAID);
        List<PaymentDTO> paymentDTOs = payments.stream()
                .map(PaymentDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(paymentDTOs);
    }

    // Lấy tất cả khoản nộp
    @GetMapping
    public ResponseEntity<List<PaymentDTO>> getAllPayments() {
        List<PaymentDTO> result = paymentRepository.findAll()
                .stream().map(PaymentDTO::new).collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    // Lấy chi tiết khoản nộp theo ID
    @GetMapping("/{id}")
    public ResponseEntity<PaymentDTO> getPaymentById(@PathVariable Long id) {
        return paymentRepository.findById(id)
                .map(payment -> ResponseEntity.ok(new PaymentDTO(payment)))
                .orElse(ResponseEntity.notFound().build());
    }

    // Cập nhật khoản nộp
    @PutMapping("/{id}")
    public ResponseEntity<?> updatePayment(@PathVariable Long id, @RequestBody CreatePaymentRequest request) {
        try {
            Payment existingPayment = paymentRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Payment not found"));

            // Lấy các entity liên quan
            Citizen citizen = citizenRepository.findById(request.getCitizenId())
                    .orElseThrow(() -> new RuntimeException("Citizen not found"));

            Accountant accountant = accountantRepository.findById(request.getAccountantId())
                    .orElseThrow(() -> new RuntimeException("Accountant not found"));

            Resident resident = residentRepository.findById(request.getResidentId())
                    .orElseThrow(() -> new RuntimeException("Resident not found"));

            // Cập nhật CollectionPeriod của payment
            CollectionPeriod cp = existingPayment.getCollectionPeriod();
            if (cp == null) {
                cp = new CollectionPeriod();
            }
            cp.setCpName(request.getCpName());
            cp.setCollectionDate(LocalDate.parse(request.getCollectionDate()));
            cp.setAccountant(accountant);
            cp.setResident(resident);
            collectionPeriodRepository.save(cp);

            // Cập nhật Payment
            existingPayment.setCitizen(citizen);
            existingPayment.setCollectionPeriod(cp);
            existingPayment.setCost(request.getCost().doubleValue());
            existingPayment.setStatus(PaymentStatus.valueOf(request.getStatus()));

            paymentRepository.save(existingPayment);

            return ResponseEntity.ok(new PaymentDTO(existingPayment));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }


    // Xoá khoản nộp
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePayment(@PathVariable Long id) {
        if (!paymentRepository.existsById(id)) return ResponseEntity.notFound().build();
        paymentRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping
    public ResponseEntity<?> createPayment(@RequestBody CreatePaymentRequest request) {
        try {
            if (request.getCitizenId() == null || request.getAccountantId() == null || request.getResidentId() == null) {
                return ResponseEntity.badRequest().body("Missing required fields");
            }

            Citizen citizen = citizenRepository.findById(request.getCitizenId())
                    .orElseThrow(() -> new RuntimeException("Citizen not found"));

            Accountant accountant = accountantRepository.findById(request.getAccountantId())
                    .orElseThrow(() -> new RuntimeException("Accountant not found"));

            Resident resident = residentRepository.findById(request.getResidentId())
                    .orElseThrow(() -> new RuntimeException("Resident not found"));

            // Tạo mới CollectionPeriod
            CollectionPeriod cp = new CollectionPeriod();
            cp.setCpName(request.getCpName());
            cp.setCollectionDate(LocalDate.parse(request.getCollectionDate()));
            cp.setAccountant(accountant);
            cp.setResident(resident);
            collectionPeriodRepository.save(cp);

            // Tạo mới Payment
            Payment payment = new Payment();
            payment.setCitizen(citizen);
            payment.setCollectionPeriod(cp);
            payment.setCost(request.getCost().doubleValue());
            payment.setStatus(PaymentStatus.valueOf(request.getStatus()));

            Payment savedPayment = paymentRepository.save(payment);

            // Trả về PaymentDTO (JSON)
            return ResponseEntity.ok(new PaymentDTO(savedPayment));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }


}