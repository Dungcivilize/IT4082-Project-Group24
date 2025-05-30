package com.KTPM.KTPM.Controllers;

import com.KTPM.KTPM.DTO.AddPaymentRequest;
import com.KTPM.KTPM.DTO.PaymentDTO;
import com.KTPM.KTPM.Models.Accountant;
import com.KTPM.KTPM.Models.CollectionPeriod;
import com.KTPM.KTPM.Models.Payment;
import com.KTPM.KTPM.Models.Citizen;
import com.KTPM.KTPM.Models.Payment.PaymentStatus;
import com.KTPM.KTPM.Models.Resident;
import com.KTPM.KTPM.Repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    private ResidentRepository residentRepository;

    @Autowired
    private AccountantRepository accountantRepository;

    @Autowired
    private CitizenRepository citizenRepository;

    @Autowired private CollectionPeriodRepository collectionPeriodRepository;

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

    // Tạo khoản nộp mới
    @PostMapping
    public ResponseEntity<Payment> createPayment(@RequestBody Payment payment) {
        Payment saved = paymentRepository.save(payment);
        return ResponseEntity.ok(saved);
    }

    // Cập nhật khoản nộp
    @PutMapping("/{id}")
    public ResponseEntity<Payment> updatePayment(@PathVariable Long id, @RequestBody Payment updatedPayment) {
        return paymentRepository.findById(id).map(existing -> {
            existing.setCitizen(updatedPayment.getCitizen());
            existing.setCollectionPeriod(updatedPayment.getCollectionPeriod());
            existing.setCost(updatedPayment.getCost());
            existing.setStatus(updatedPayment.getStatus());
            return ResponseEntity.ok(paymentRepository.save(existing));
        }).orElse(ResponseEntity.notFound().build());
    }

    // Xoá khoản nộp
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePayment(@PathVariable Long id) {
        if (!paymentRepository.existsById(id)) return ResponseEntity.notFound().build();
        paymentRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    // Thêm khoản nộp
    @PostMapping("/add-payment")
    public ResponseEntity<String> addPayment(@RequestBody AddPaymentRequest request) {
        try {
            // Lấy citizen từ citizenId
            Citizen citizen = citizenRepository.findById(request.getCitizenId())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy citizen với ID: " + request.getCitizenId()));

            // Lấy resident từ citizen (giả sử mỗi citizen có 1 resident liên kết)
            Long residentId = citizen.getResidentId();
            Resident resident = residentRepository.findById(residentId)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy resident với ID: " + residentId));


            // Lấy accountant từ accountantId
            Accountant accountant = accountantRepository.findById(request.getAccountantId())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy accountant với ID: " + request.getAccountantId()));

            // B1: Tạo collection period
            CollectionPeriod newPeriod = new CollectionPeriod();
            newPeriod.setCpName(request.getCpName());
            newPeriod.setResident(resident); // gán resident từ citizen
            newPeriod.setAccountant(accountant);
            newPeriod.setCollectionDate(
                    request.getCollectionDate() != null ? request.getCollectionDate() : LocalDate.now()
            );
            CollectionPeriod savedPeriod = collectionPeriodRepository.save(newPeriod);

            // B2: Tạo payment
            Payment payment = new Payment();
            payment.setCitizen(citizen); // đúng kiểu rồi
            payment.setCollectionPeriod(savedPeriod);
            payment.setCost(request.getCost());
            payment.setStatus(Payment.PaymentStatus.valueOf(request.getStatus())); // phải là PAID/UNPAID
            paymentRepository.save(payment);

            return ResponseEntity.ok("Thêm khoản nộp thành công.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Lỗi: " + e.getMessage());
        }
    }


}