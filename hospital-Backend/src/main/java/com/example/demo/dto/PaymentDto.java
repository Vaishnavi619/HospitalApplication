package com.example.demo.dto;

public class PaymentDto{
    private double amount;
    private int patientId;
	public PaymentDto(double amount, int patientId) {
		super();
		this.amount = amount;
		this.patientId = patientId;
	}
	public PaymentDto() {
		super();
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public int getPatientId() {
		return patientId;
	}
	public void setPatientId(int patientId) {
		this.patientId = patientId;
	}

   
}

