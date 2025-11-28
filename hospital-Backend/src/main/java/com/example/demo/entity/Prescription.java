package com.example.demo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;


import java.time.LocalDate;



import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class Prescription {

 @Id
 @GeneratedValue(strategy = GenerationType.IDENTITY)
 private int id;

 private String diagnosis;

 private LocalDate date;

 @ManyToOne
 private Appointment appointment;

 @ManyToOne
 private Doctor doctor;

 @ManyToOne
 private Patient patient;

 @Lob
 @Column(name = "medicines_json", columnDefinition = "LONGTEXT")
 private String medicinesJson;

 public Prescription(String diagnosis, LocalDate date, Appointment appointment, Doctor doctor, Patient patient,
		String medicinesJson) {
	super();
	this.diagnosis = diagnosis;
	this.date = date;
	this.appointment = appointment;
	this.doctor = doctor;
	this.patient = patient;
	this.medicinesJson = medicinesJson;
 }

 public Prescription() {
	super();
 }

 public int getId() {
	return id;
 }

 public void setId(int id) {
	this.id = id;
 }

 public String getDiagnosis() {
	return diagnosis;
 }

 public void setDiagnosis(String diagnosis) {
	this.diagnosis = diagnosis;
 }

 public LocalDate getDate() {
	return date;
 }

 public void setDate(LocalDate date) {
	this.date = date;
 }

 public Appointment getAppointment() {
	return appointment;
 }

 public void setAppointment(Appointment appointment) {
	this.appointment = appointment;
 }

 public Doctor getDoctor() {
	return doctor;
 }

 public void setDoctor(Doctor doctor) {
	this.doctor = doctor;
 }

 public Patient getPatient() {
	return patient;
 }

 public void setPatient(Patient patient) {
	this.patient = patient;
 }

 public String getMedicinesJson() {
	return medicinesJson;
 }

 public void setMedicinesJson(String medicinesJson) {
	this.medicinesJson = medicinesJson;
 } 

}
