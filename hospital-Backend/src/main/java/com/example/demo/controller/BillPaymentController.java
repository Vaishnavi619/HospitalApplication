package com.example.demo.controller;

import com.example.demo.entity.Bill;
import com.example.demo.ennums.PaymentStatus;
import com.example.demo.repository.BillRepository;
import com.example.demo.service.QrService;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import jakarta.transaction.Transactional;

import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/bills")
@CrossOrigin(origins = "http://localhost:4200")
public class BillPaymentController {

    private final BillRepository billRepository;
    private final QrService qrService;

    @Value("${upi.payee.vpa:hospital@upi}")
    private String upiPayeeVpa;

    @Value("${upi.payee.name:Hospital}")
    private String upiPayeeName;

    @Value("${qr.expire.minutes:10}")
    private int qrExpireMinutes;

    public BillPaymentController(BillRepository billRepository, QrService qrService) {
        this.billRepository = billRepository;
        this.qrService = qrService;
    }

    @GetMapping("/{billId}/upi-qr")
    @Transactional
    public ResponseEntity<?> generateUpiQr(@PathVariable int billId, Authentication authentication) {
        Bill bill = billRepository.findById(billId)
                .orElseThrow(() -> new RuntimeException("Bill not found with id: " + billId));

        if (!isOwnerOrAdmin(authentication, bill)) {
            return ResponseEntity.status(403).body("Forbidden");
        }

        bill = qrService.generateUpiQrForBill(bill);
        bill.setQrExpiresAt(LocalDateTime.now().plusMinutes(qrExpireMinutes));
        bill.setPaymentStatus(PaymentStatus.UNPAID);

        billRepository.save(bill);

        Map<String, Object> resp = new HashMap<>();
        resp.put("qrCodeUrl", bill.getQrCodeUrl());
        resp.put("upiUri", qrService.buildUpiUri(upiPayeeVpa, upiPayeeName, bill.getTotalAmount(), bill.getBillId()));
        resp.put("expiresAt", bill.getQrExpiresAt());
        resp.put("amount", bill.getTotalAmount());

        return ResponseEntity.ok(resp);
    }

    @PostMapping("/{billId}/confirm-manual")
    @Transactional
    public ResponseEntity<?> confirmManualPayment(@PathVariable int billId,
                                                  @RequestBody Map<String, String> body,
                                                  Authentication authentication) {
        Bill bill = billRepository.findById(billId)
                .orElseThrow(() -> new RuntimeException("Bill not found with id: " + billId));

        if (!isOwnerOrAdmin(authentication, bill)) {
            return ResponseEntity.status(403).body("Forbidden");
        }

        String txnRef = body.get("txnRef");
        if (txnRef == null || txnRef.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("txnRef is required");
        }

        bill.setPaymentReference(txnRef.trim());
        bill.setPaymentStatus(PaymentStatus.PAID);
        billRepository.save(bill);

        Map<String, Object> resp = new HashMap<>();
        resp.put("message", "Payment marked as PAID (manual).");
        resp.put("paymentReference", txnRef.trim());

        return ResponseEntity.ok(resp);
    }

    private boolean isOwnerOrAdmin(Authentication authentication, Bill bill) {
        if (authentication == null) return false;
        String username = authentication.getName();
        try {
            if (bill.getPatient() != null && bill.getPatient().getUser() != null) {
                return username.equals(bill.getPatient().getUser().getUsername()) || hasRoleAdmin(authentication);
            }
        } catch (Exception ignored) {}
        return hasRoleAdmin(authentication);
    }

    private boolean hasRoleAdmin(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_SUPERADMIN") || a.getAuthority().equals("ROLE_ADMIN"));
    }
    @GetMapping(value = "/{billId}/upi-qr-image", produces = MediaType.IMAGE_PNG_VALUE)
    @Transactional
    public ResponseEntity<byte[]> generateUpiQrImage(@PathVariable int billId, Authentication authentication) {
        Bill bill = billRepository.findById(billId)
                .orElseThrow(() -> new RuntimeException("Bill not found with id: " + billId));

        // Security: only owner or admin can generate
        if (!isOwnerOrAdmin(authentication, bill)) {
            return ResponseEntity.status(403).build();
        }

        try {
            // Build the UPI URI (reuse your qrService builder for consistent formatting)
            String upiUri = qrService.buildUpiUri(upiPayeeVpa, upiPayeeName, bill.getTotalAmount(), bill.getBillId());

            // Generate PNG bytes for the QR code
            byte[] png = generateQrPngBytes(upiUri, 400); // 400x400 px - adjust if needed

            // Optional: save base64 to DB so next time you can fetch it quickly (uncomment if desired)
            String base64 = Base64.getEncoder().encodeToString(png);
            bill.setQrCodeUrl("data:image/png;base64," + base64);
            bill.setQrExpiresAt(LocalDateTime.now().plusMinutes(qrExpireMinutes));
            bill.setPaymentStatus(PaymentStatus.UNPAID);
            billRepository.save(bill);

            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_PNG)
                    .contentLength(png.length)
                    .body(png);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }
    private byte[] generateQrPngBytes(String text, int size) throws Exception {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, size, size);

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", baos);
            return baos.toByteArray();
        }
    }
}
