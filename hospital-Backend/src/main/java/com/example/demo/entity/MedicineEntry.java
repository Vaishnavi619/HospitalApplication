package com.example.demo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class MedicineEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    private Prescription prescription;

    @ManyToOne
    private Medicine medicine; 

    private String dosage;   
    private boolean morning;
    private boolean afternoon;
    private boolean night;
    private String notes;    // optional
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Prescription getPrescription() {
		return prescription;
	}
	public void setPrescription(Prescription prescription) {
		this.prescription = prescription;
	}
	public Medicine getMedicine() {
		return medicine;
	}
	public void setMedicine(Medicine medicine) {
		this.medicine = medicine;
	}
	public String getDosage() {
		return dosage;
	}
	public void setDosage(String dosage) {
		this.dosage = dosage;
	}
	public boolean isMorning() {
		return morning;
	}
	public void setMorning(boolean morning) {
		this.morning = morning;
	}
	public boolean isAfternoon() {
		return afternoon;
	}
	public void setAfternoon(boolean afternoon) {
		this.afternoon = afternoon;
	}
	public boolean isNight() {
		return night;
	}
	public void setNight(boolean night) {
		this.night = night;
	}
	public String getNotes() {
		return notes;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}
	public MedicineEntry(Prescription prescription, Medicine medicine, String dosage, boolean morning,
			boolean afternoon, boolean night, String notes) {
		super();
		this.prescription = prescription;
		this.medicine = medicine;
		this.dosage = dosage;
		this.morning = morning;
		this.afternoon = afternoon;
		this.night = night;
		this.notes = notes;
	}
	public MedicineEntry() {
		super();
	}
    	
    

}
