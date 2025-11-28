package com.example.demo.service;

import com.example.demo.dto.PaymentDto;
import com.razorpay.Order;
import com.razorpay.RazorpayException;

public interface PaymentService {
    Order createOrder(PaymentDto paymentDto) throws RazorpayException;
    String savePayment(String razorpayPaymentId, String razorpayOrderId, String status);
}
