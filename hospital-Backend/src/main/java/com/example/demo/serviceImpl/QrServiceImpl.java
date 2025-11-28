package com.example.demo.serviceImpl;

import com.example.demo.entity.Bill;
import com.example.demo.service.QrService;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Base64;

@Service
public class QrServiceImpl implements QrService {

    @Override
    public String buildUpiUri(String vpa, String name, double amount, int billId) {
        BigDecimal amt = BigDecimal.valueOf(amount).setScale(2, BigDecimal.ROUND_HALF_UP);
        return String.format(
                "upi://pay?pa=%s&pn=%s&am=%s&cu=INR&tn=Bill%d",
                vpa,
                name,
                amt.toPlainString(),
                billId
        );
    }

    @Override
    public String generateQrBase64(String upiUri, int size) {
        try {
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(upiUri, BarcodeFormat.QR_CODE, size, size);

            BufferedImage bufferedImage = MatrixToImageWriter.toBufferedImage(bitMatrix);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "png", baos);

            return Base64.getEncoder().encodeToString(baos.toByteArray());
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate QR code", e);
        }
    }

    @Override
    public Bill generateUpiQrForBill(Bill bill) {
        String vpa = bill.getUpiId() != null ? bill.getUpiId() : "hospital@upi";
        String upiUri = buildUpiUri(vpa, "Hospital", bill.getTotalAmount(), bill.getBillId());

        String qrBase64 = generateQrBase64(upiUri, 300);

        bill.setQrCodeUrl("data:image/png;base64," + qrBase64);
        bill.setUpiId(vpa);
        bill.setPaymentReference("BILL-" + bill.getBillId() + "-" + System.currentTimeMillis());
        bill.setQrExpiresAt(LocalDateTime.now().plusMinutes(10));

        return bill;
    }
}
