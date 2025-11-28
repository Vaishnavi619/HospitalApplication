package com.example.demo.controller;

import com.example.demo.dto.PaymentDto;
import com.example.demo.dto.PaymentResponseDto;
import com.example.demo.ennums.PaymentStatus;
import com.example.demo.entity.Bill;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.BillRepository;
import com.example.demo.service.PaymentService;
import com.example.demo.utility.ResponseStructure;
import com.razorpay.Order;
import com.razorpay.RazorpayException;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
@CrossOrigin(origins = "http://localhost:4200")
public class PaymentController {

    private final PaymentService paymentService;
    private final BillRepository billRepository;

    public PaymentController(PaymentService paymentService, BillRepository billRepository) {
        this.paymentService = paymentService;
        this.billRepository = billRepository;
    }
    @PreAuthorize("hasRole('PATIENT')")
    @PostMapping("/create-order")
    public ResponseEntity<String> createOrder(@RequestBody PaymentDto paymentDto) {
        try {
            Order order = paymentService.createOrder(paymentDto);
            return ResponseEntity.ok(order.toString());
        } catch (RazorpayException e) {
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }
    @PreAuthorize("hasRole('PATIENT')")
    @PostMapping("/confirm")
    public ResponseEntity<String> confirmPayment(
            @RequestParam String razorpayPaymentId,
            @RequestParam String razorpayOrderId,
            @RequestParam String status
    ) {
        String result = paymentService.savePayment(razorpayPaymentId, razorpayOrderId, status);
        return ResponseEntity.ok(result);
    }
    @PreAuthorize("hasRole('PATIENT')")
    @PostMapping("/save")
    public ResponseEntity<ResponseStructure<String>> savePaymentStatus(@RequestBody PaymentResponseDto paymentResponse) {
        Optional<Bill> optionalBill = billRepository.findById(paymentResponse.getBillId());
        
        if (optionalBill.isPresent()) {
            Bill bill = optionalBill.get();
            bill.setPaymentStatus(PaymentStatus.PAID);
            billRepository.save(bill);

            ResponseStructure<String> response = new ResponseStructure<>();
            response.setStatuscode(HttpStatus.OK.value());
            response.setMessage("Payment recorded successfully");
            response.setData("Payment ID: " + paymentResponse.getRazorpayPaymentId());

            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            throw new ResourceNotFoundException("Bill not found with ID: " + paymentResponse.getBillId());
        }
    }

}
