package com.example.demo.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.PrescriptionDto;
import com.example.demo.entity.Prescription;
import com.example.demo.service.PrescriptionService;
import com.example.demo.utility.ResponseStructure;

@RestController
@RequestMapping("/api/prescriptions")
@CrossOrigin(origins = "http://localhost:4200")
public class PrescriptionController {

    private final PrescriptionService prescriptionService;

    public PrescriptionController(PrescriptionService prescriptionService) {
        this.prescriptionService = prescriptionService;
    }
	
	@PreAuthorize("hasAnyRole('DOCTOR','RECEPTIONIST','SUPER_ADMIN')")
    @PostMapping("/{appointmentId}")
	public ResponseEntity<ResponseStructure<Prescription>> createPrescription(
	        @PathVariable int appointmentId,
	        @RequestBody PrescriptionDto dto) {

	    return prescriptionService.savePrescription(appointmentId, dto);
	}

	@PreAuthorize("hasAnyRole('DOCTOR','RECEPTIONIST','SUPER_ADMIN','PATIENT')")
	@GetMapping
	public ResponseEntity<ResponseStructure<List<Prescription>>> getAllPrescriptions(){
		return prescriptionService.getAllPrescriptions();
	}
	
	@PreAuthorize("hasAnyRole('DOCTOR','RECEPTIONIST','SUPER_ADMIN','PATIENT')")
	@GetMapping("/{prescriptionId}")
	public ResponseEntity<ResponseStructure<Prescription>> getPrescriptionById(@PathVariable int prescriptionId){
		return prescriptionService.getPrescriptionById(prescriptionId);
	}
	
	@PreAuthorize("hasAnyRole('DOCTOR','RECEPTIONIST','SUPER_ADMIN','PATIENT')")
	@DeleteMapping("/{prescriptionId}")
	public ResponseEntity<ResponseStructure<Prescription>> deletePrescription(@PathVariable int prescriptionId){
		return prescriptionService.deletePrescription(prescriptionId);
	}
	
	@PreAuthorize("hasAnyRole('DOCTOR','RECEPTIONIST','SUPER_ADMIN','PATIENT')")
	@PutMapping("/{prescriptionId}")
	public ResponseEntity<ResponseStructure<Prescription>> updatePrescription(@PathVariable   int prescriptionId,@RequestBody  Prescription updatedPrescription ){
		return prescriptionService.updatePrescription(prescriptionId,updatedPrescription);
	}
	@PreAuthorize("hasAnyRole('DOCTOR','RECEPTIONIST','SUPER_ADMIN','PATIENT')")
	@GetMapping("/doctor/view-prescriptions")
	public ResponseEntity<ResponseStructure<List<Prescription>>> getPrescriptionsForLoggedInDoctor() {
	    return prescriptionService.getPrescriptionsForLoggedInDoctor();
	}
	@PreAuthorize("hasAnyRole('PATIENT','RECEPTIONIST','SUPER_ADMIN','PATIENT')")
	@GetMapping("/patient/view-prescriptions")
	public ResponseEntity<ResponseStructure<List<Prescription>>> getPrescriptionsForLoggedInPatient() {
	    return prescriptionService.getPrescriptionsForLoggedInPatient();
	}
	// in PrescriptionController.java
	@PreAuthorize("hasAnyRole('RECEPTIONIST','DOCTOR','SUPER_ADMIN','PATIENT')")
	@GetMapping("/patient/{patientId}")
	public ResponseEntity<ResponseStructure<List<Prescription>>> getPrescriptionsByPatient(@PathVariable int patientId) {
	    return prescriptionService.getPrescriptionsByPatient(patientId);
	}


}
