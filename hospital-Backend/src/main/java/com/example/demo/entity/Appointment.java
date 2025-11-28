package com.example.demo.entity;

import java.time.LocalDate;
import java.time.LocalTime;

import com.example.demo.ennums.AppointmentStatus;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(
	    name = "appointments",
	    uniqueConstraints = {
	        @UniqueConstraint(
	            columnNames = {"doctor_id", "appointmentDate", "startTime"}
	        )
	    }
	)
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int appointmentId;

    private LocalDate appointmentDate;
    private LocalTime appointmentTime;
   
    private String reason;
    private LocalTime startTime; 
    private LocalTime endTime; 
    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;
    
   
    @Enumerated(EnumType.STRING)
    private AppointmentStatus status = AppointmentStatus.BOOKED;

	public Appointment() {
		super();
	}

	public Appointment(LocalDate appointmentDate, LocalTime appointmentTime, String reason, LocalTime startTime,
			LocalTime endTime, Patient patient, Doctor doctor, AppointmentStatus status) {
		super();
		this.appointmentDate = appointmentDate;
		this.appointmentTime = appointmentTime;
		this.reason = reason;
		this.startTime = startTime;
		this.endTime = endTime;
		this.patient = patient;
		this.doctor = doctor;
		this.status = status;
	}

	public final int getAppointmentId() {
		return appointmentId;
	}

	public final void setAppointmentId(int appointmentId) {
		this.appointmentId = appointmentId;
	}

	public final LocalDate getAppointmentDate() {
		return appointmentDate;
	}

	public final void setAppointmentDate(LocalDate appointmentDate) {
		this.appointmentDate = appointmentDate;
	}

	public final LocalTime getAppointmentTime() {
		return appointmentTime;
	}

	public final void setAppointmentTime(LocalTime appointmentTime) {
		this.appointmentTime = appointmentTime;
	}

	public final String getReason() {
		return reason;
	}

	public final void setReason(String reason) {
		this.reason = reason;
	}

	public final LocalTime getStartTime() {
		return startTime;
	}

	public final void setStartTime(LocalTime startTime) {
		this.startTime = startTime;
	}

	public final LocalTime getEndTime() {
		return endTime;
	}

	public final void setEndTime(LocalTime endTime) {
		this.endTime = endTime;
	}

	public final Patient getPatient() {
		return patient;
	}

	public final void setPatient(Patient patient) {
		this.patient = patient;
	}

	public final Doctor getDoctor() {
		return doctor;
	}

	public final void setDoctor(Doctor doctor) {
		this.doctor = doctor;
	}

	public final AppointmentStatus getStatus() {
		return status;
	}

	public final void setStatus(AppointmentStatus status) {
		this.status = status;
	}

	

	
    
}
