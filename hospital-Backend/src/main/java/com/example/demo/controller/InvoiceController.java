package com.example.demo.controller;

import java.io.ByteArrayInputStream;

import com.example.demo.exception.PdfProcessingException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.service.InvoiceService;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/invoices")
public class InvoiceController {

    private final InvoiceService invoiceService;

    public InvoiceController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }
    @PreAuthorize("hasRole('PATIENT')")
    @GetMapping("/{billId}/download")
    public ResponseEntity<byte[]> downloadInvoice(@PathVariable int billId) {
        ByteArrayInputStream bis = invoiceService.generateInvoice(billId);
        byte[] pdfBytes;
        try {
            pdfBytes = bis.readAllBytes();
        } catch (Exception e) {
            throw new PdfProcessingException("Error reading PDF stream", e);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=invoice_" + billId + ".pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfBytes);
    }

}
