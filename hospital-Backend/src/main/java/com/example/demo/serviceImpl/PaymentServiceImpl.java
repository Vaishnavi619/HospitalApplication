package com.example.demo.serviceImpl;

import com.example.demo.dto.PaymentDto;
import com.example.demo.entity.Patient;
import com.example.demo.entity.Payment;
import com.example.demo.repository.PatientRepository;
import com.example.demo.repository.PaymentRepository;
import com.example.demo.service.PaymentService;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    private RazorpayClient client;

    public PaymentServiceImpl() throws RazorpayException {
        this.client = new RazorpayClient("rzp_test_ONcTecOTfNR4YV", "PQou2nyTNdDziliEopxsgb4a"); 
    }

 
    @Override
    public Order createOrder(PaymentDto paymentRequest) throws RazorpayException {
        JSONObject options = new JSONObject();
        options.put("amount", (int)(paymentRequest.getAmount() * 100));
        options.put("currency", "INR");
        options.put("receipt", "txn_" + System.currentTimeMillis());

        Order order = client.orders.create(options);

        Optional<Patient> optionalPatient = patientRepository.findById(paymentRequest.getPatientId());
        if (optionalPatient.isPresent()) {
            Payment payment = new Payment();
            payment.setAmount(paymentRequest.getAmount());
            payment.setRazorpayOrderId(order.get("id"));
            payment.setStatus("CREATED");
            payment.setCreatedAt(LocalDateTime.now());
            payment.setPatient(optionalPatient.get());

            paymentRepository.save(payment);
        }

        return order;
    }

  
    @Override
    public String savePayment(String paymentId, String orderId, String status) {
        Payment payment = paymentRepository.findAll()
                .stream()
                .filter(p -> p.getRazorpayOrderId().equals(orderId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Order not found"));

        payment.setPaymentId(paymentId);
        payment.setStatus(status);

        paymentRepository.save(payment);

        return "Payment saved successfully!";
    }
}
