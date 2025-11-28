package com.example.demo.service;

import java.io.ByteArrayInputStream;

public interface InvoiceService {
	ByteArrayInputStream generateInvoice(int billId);
}
