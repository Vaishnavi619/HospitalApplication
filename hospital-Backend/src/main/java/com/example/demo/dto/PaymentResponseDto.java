package com.example.demo.dto;

public class PaymentResponseDto {
    private String razorpayPaymentId;
    private String razorpayOrderId;
    private String status;
    private int billId;
	public PaymentResponseDto(String razorpayPaymentId, String razorpayOrderId, String status, int billId) {
		super();
		this.razorpayPaymentId = razorpayPaymentId;
		this.razorpayOrderId = razorpayOrderId;
		this.status = status;
		this.billId = billId;
	}
	public PaymentResponseDto() {
		super();
	}
	public String getRazorpayPaymentId() {
		return razorpayPaymentId;
	}
	public void setRazorpayPaymentId(String razorpayPaymentId) {
		this.razorpayPaymentId = razorpayPaymentId;
	}
	public String getRazorpayOrderId() {
		return razorpayOrderId;
	}
	public void setRazorpayOrderId(String razorpayOrderId) {
		this.razorpayOrderId = razorpayOrderId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public int getBillId() {
		return billId;
	}
	public void setBillId(int billId) {
		this.billId = billId;
	}
    
    
}
