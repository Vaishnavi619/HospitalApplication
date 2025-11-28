package com.example.demo.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.example.demo.ennums.PaymentMethod;
import com.example.demo.ennums.PaymentStatus;
import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;


@Entity
@Table(name="bills")
public class Bill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int billId;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate billDate;

    private double medicineCharges;
    private double consultationFee;
    private double totalAmount;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus = PaymentStatus.UNPAID; 
    
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;  


   
    private String upiId;       
    @Lob
    @Column(name = "qr_code_url", columnDefinition = "LONGTEXT")
    private String qrCodeUrl;
   
    private String paymentReference; 
    private LocalDateTime qrExpiresAt;

    @OneToOne
    @JoinColumn(name = "prescription_id")
    private Prescription prescription;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;

    public Bill() {}

   public Bill(LocalDate billDate, double medicineCharges, double consultationFee, double totalAmount,
                PaymentStatus paymentStatus, Prescription prescription, Patient patient,LocalDateTime qrExpiresAt,PaymentMethod paymentMethod) {
        this.billDate = billDate;
        this.medicineCharges = medicineCharges;
        this.consultationFee = consultationFee;
        this.totalAmount = totalAmount;
        this.paymentStatus = paymentStatus;
        this.prescription = prescription;
        this.patient = patient;
        this.qrExpiresAt=qrExpiresAt;
        this.paymentMethod=paymentMethod;
    }

    // ---------------- Getters & Setters ----------------
    public int getBillId() {
        return billId;
    }

    public void setBillId(int billId) {
        this.billId = billId;
    }

    public LocalDate getBillDate() {
        return billDate;
    }

    public void setBillDate(LocalDate billDate) {
        this.billDate = billDate;
    }

    public double getMedicineCharges() {
        return medicineCharges;
    }

    public void setMedicineCharges(double medicineCharges) {
        this.medicineCharges = medicineCharges;
    }

    public double getConsultationFee() {
        return consultationFee;
    }

    public void setConsultationFee(double consultationFee) {
        this.consultationFee = consultationFee;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public Prescription getPrescription() {
        return prescription;
    }

    public void setPrescription(Prescription prescription) {
        this.prescription = prescription;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public String getUpiId() {
        return upiId;
    }

    public void setUpiId(String upiId) {
        this.upiId = upiId;
    }

    public String getQrCodeUrl() {
        return qrCodeUrl;
    }

    public void setQrCodeUrl(String qrCodeUrl) {
        this.qrCodeUrl = qrCodeUrl;
    }

    public String getPaymentReference() {
        return paymentReference;
    }

    public void setPaymentReference(String paymentReference) {
        this.paymentReference = paymentReference;
    }

	public LocalDateTime getQrExpiresAt() {
		return qrExpiresAt;
	}

	public void setQrExpiresAt(LocalDateTime qrExpiresAt) {
		this.qrExpiresAt = qrExpiresAt;
	}

	public PaymentMethod getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(PaymentMethod paymentMethod) {
		this.paymentMethod = paymentMethod;
	}
    
    
}
