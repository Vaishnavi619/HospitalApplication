package com.example.demo.entity;


import java.time.DayOfWeek;
import java.time.LocalTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "doctor_availability")
public class DoctorAvailability {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int doctorAvailibilityId;

    @ManyToOne
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor; // ✅ No separate int doctorId here

    private DayOfWeek dayOfWeek;  // MONDAY, TUESDAY, etc.
    private LocalTime startTime;  // Example: 09:00
    private LocalTime endTime;    // Example: 12:00
	public DoctorAvailability() {
		super();
	}
	public DoctorAvailability(Doctor doctor, DayOfWeek dayOfWeek, LocalTime startTime, LocalTime endTime) {
		super();
		this.doctor = doctor;
		this.dayOfWeek = dayOfWeek;
		this.startTime = startTime;
		this.endTime = endTime;
	}
	public int getDoctorAvailibilityId() {
		return doctorAvailibilityId;
	}
	public void setDoctorAvailibilityId(int doctorAvailibilityId) {
		this.doctorAvailibilityId = doctorAvailibilityId;
	}
	public Doctor getDoctor() {
		return doctor;
	}
	public void setDoctor(Doctor doctor) {
		this.doctor = doctor;
	}
	public DayOfWeek getDayOfWeek() {
		return dayOfWeek;
	}
	public void setDayOfWeek(DayOfWeek dayOfWeek) {
		this.dayOfWeek = dayOfWeek;
	}
	public LocalTime getStartTime() {
		return startTime;
	}
	public void setStartTime(LocalTime startTime) {
		this.startTime = startTime;
	}
	public LocalTime getEndTime() {
		return endTime;
	}
	public void setEndTime(LocalTime endTime) {
		this.endTime = endTime;
	}
    
    
  
}
