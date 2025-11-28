package com.example.demo.dto;

public class BillDto {
    private int billId;
    private PatientDto patient;
    private int prescriptionId;
    private String diagnosis;
    private double consultationFee;
    private double medicineCharges;
    private double totalAmount;
    private String billDate;

    private String paymentStatus; 

	public BillDto(PatientDto patient, int prescriptionId, String diagnosis, double consultationFee,
			double medicineCharges, double totalAmount, String billDate,String paymentStatus) {
		super();
		this.patient = patient;
		this.prescriptionId = prescriptionId;
		this.diagnosis = diagnosis;
		this.consultationFee = consultationFee;
		this.medicineCharges = medicineCharges;
		this.totalAmount = totalAmount;
		this.billDate = billDate;
		this.paymentStatus=paymentStatus;
	}
	public BillDto() {
		super();
	}
	public int getBillId() {
		return billId;
	}
	public void setBillId(int billId) {
		this.billId = billId;
	}
	public PatientDto getPatient() {
		return patient;
	}
	public void setPatient(PatientDto patient) {
		this.patient = patient;
	}
	public int getPrescriptionId() {
		return prescriptionId;
	}
	public void setPrescriptionId(int prescriptionId) {
		this.prescriptionId = prescriptionId;
	}
	public String getDiagnosis() {
		return diagnosis;
	}
	public void setDiagnosis(String diagnosis) {
		this.diagnosis = diagnosis;
	}
	public double getConsultationFee() {
		return consultationFee;
	}
	public void setConsultationFee(double consultationFee) {
		this.consultationFee = consultationFee;
	}
	public double getMedicineCharges() {
		return medicineCharges;
	}
	public void setMedicineCharges(double medicineCharges) {
		this.medicineCharges = medicineCharges;
	}
	public double getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}
	public String getBillDate() {
		return billDate;
	}
	public void setBillDate(String billDate) {
		this.billDate = billDate;
	}
	public String getPaymentStatus() {
		return paymentStatus;
	}
	public void setPaymentStatus(String paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

    
}
