package KTPM.Backend.accountant.controller;

import KTPM.Backend.accountant.dto.AccountantPaymentPeriodResponse;
import KTPM.Backend.accountant.service.AccountantPaymentPeriodService;
import KTPM.Backend.entity.PaymentPeriod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accountant/payment-periods")
public class AccountantPaymentPeriodController {

    private final AccountantPaymentPeriodService service;

    public AccountantPaymentPeriodController(AccountantPaymentPeriodService service) {
        this.service = service;
    }

    // GET lấy danh sách kỳ thu phí
    @GetMapping
    public List<AccountantPaymentPeriodResponse> getAllPaymentPeriods() {
        return service.getAllPaymentPeriods();
    }

    // GET lấy chi tiết kỳ thu phí theo id
    @GetMapping("/{paymentPeriodId}")
    public AccountantPaymentPeriodResponse getPaymentPeriodDetail(@PathVariable Integer paymentPeriodId) {
        return service.getPaymentPeriodDetail(paymentPeriodId);
    }

    // POST tạo đợt thu phí mới
    @PostMapping
    public ResponseEntity<PaymentPeriod> createPaymentPeriod(@RequestBody PaymentPeriodRequest request) {
        PaymentPeriod created = service.createPaymentPeriodWithDetails(request.getMonth(), request.getYear(), request.getNote());
        return ResponseEntity.ok(created);
    }

    public static class PaymentPeriodRequest {
        private Integer month;
        private Integer year;
        private String note;

        public Integer getMonth() { return month; }
        public void setMonth(Integer month) { this.month = month; }

        public Integer getYear() { return year; }
        public void setYear(Integer year) { this.year = year; }

        public String getNote() { return note; }
        public void setNote(String note) { this.note = note; }
    }
}
