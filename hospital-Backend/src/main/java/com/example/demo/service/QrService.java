package com.example.demo.service;

import com.example.demo.entity.Bill;

public interface QrService {
    String buildUpiUri(String vpa, String name, double amount, int billId);

    String generateQrBase64(String upiUri, int size);

    Bill generateUpiQrForBill(Bill bill);
}
