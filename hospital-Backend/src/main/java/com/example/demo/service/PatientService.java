package com.example.demo.service;

import java.util.List;


import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.dto.PatientDto;
import com.example.demo.entity.Appointment;
import com.example.demo.entity.Patient;

import com.example.demo.utility.ResponseStructure;

public interface PatientService {

	public ResponseEntity<ResponseStructure<Patient>>  savePatient(PatientDto patientDto);
	public  ResponseEntity<ResponseStructure<List<Patient>>> getAllPatients();
	public ResponseEntity<ResponseStructure<Patient>> getPatientById(int patientId);
	public ResponseEntity<ResponseStructure<Patient>>  deletePatientById(int patientId);
	public ResponseEntity<ResponseStructure<Patient>> updatePatient(int patientId,Patient patient);
	public ResponseEntity<ResponseStructure<Patient>> getLoggedInPatient();
	ResponseEntity<ResponseStructure<List<Appointment>>> getLoggedInPatientAppointments();
	public ResponseEntity<ResponseStructure<String>> saveBulkPatients(MultipartFile file);
	
	

}
