package KTPM.Backend.accountant.controller;

import java.util.List;
import java.util.Map;

import KTPM.Backend.accountant.service.AccountantPaymentService;
import KTPM.Backend.entity.Payment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/accountant/payments")
public class AccountantPaymentController {

    @Autowired
    private AccountantPaymentService paymentService;

    @PostMapping("/{paymentId}/approve")
    public ResponseEntity<?> approvePayment(@PathVariable Integer paymentId) {
        try {
            paymentService.approvePayment(paymentId);
            return ResponseEntity.ok("Duyệt thanh toán thành công");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/{paymentId}/reject")
    public ResponseEntity<?> rejectPayment(
            @PathVariable Integer paymentId,
            @RequestBody Map<String, String> body
    ) {
        String note = body.get("note");
        if (note == null || note.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Lý do từ chối không được để trống");
        }
        try {
            paymentService.rejectPayment(paymentId, note);
            return ResponseEntity.ok("Từ chối thanh toán thành công");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/processing")
    public ResponseEntity<?> getProcessingPayments() {
        try {
            List<Payment> payments = paymentService.getProcessingPayments();
            return ResponseEntity.ok(payments);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Lỗi khi lấy danh sách thanh toán đang xử lý");
        }
    }
}


