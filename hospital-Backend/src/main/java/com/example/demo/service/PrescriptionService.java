package com.example.demo.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.example.demo.dto.PrescriptionDto;
import com.example.demo.entity.Doctor;
import com.example.demo.entity.Prescription;
import com.example.demo.utility.ResponseStructure;

public interface PrescriptionService {
		public ResponseEntity<ResponseStructure<Prescription>>  savePrescription(int appointmentId, PrescriptionDto prescription);
		public ResponseEntity<ResponseStructure<List<Prescription>>> getAllPrescriptions();
		public ResponseEntity<ResponseStructure<Prescription>> getPrescriptionById(int prescriptionId);
		public ResponseEntity<ResponseStructure<Prescription>> deletePrescription(int prescriptionId);
		public ResponseEntity<ResponseStructure<Prescription>> updatePrescription(    int prescriptionId, Prescription updatedPrescription );
		public Doctor getLoggedInDoctor();
		public ResponseEntity<ResponseStructure<List<Prescription>>> getPrescriptionsForLoggedInDoctor();
		ResponseEntity<ResponseStructure<List<Prescription>>> getPrescriptionsForLoggedInPatient();
		ResponseEntity<ResponseStructure<List<Prescription>>> getPrescriptionsByPatient(int patientId);

		
	
}
