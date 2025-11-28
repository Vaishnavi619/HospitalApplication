package com.example.demo.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.dto.PatientDto;
import com.example.demo.entity.Appointment;
import com.example.demo.entity.Patient;
import com.example.demo.service.PatientService;
import com.example.demo.utility.ResponseStructure;

@RestController
@RequestMapping("/api/patients")
@CrossOrigin(origins = "http://localhost:4200")
public class PatientController {
    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }
	
	@PreAuthorize("hasAnyRole('RECEPTIONIST','ADMIN','PATIENT','SUPER_ADMIN')")
	@PostMapping
	public ResponseEntity<ResponseStructure<Patient>>  savePatient(@RequestBody   PatientDto patientDto){
		return patientService.savePatient(patientDto);
	}
	
	@PreAuthorize("hasAnyRole('RECEPTIONIST','ADMIN','PATIENT','SUPER_ADMIN')")
	@GetMapping
	public  ResponseEntity<ResponseStructure<List<Patient>>> getAllPatients(){
		return patientService.getAllPatients();
	}
	
	@PreAuthorize("hasAnyRole('RECEPTIONIST','ADMIN','PATIENT','SUPER_ADMIN')")
	@GetMapping("/{patientId}")
	public ResponseEntity<ResponseStructure<Patient>>  getPatientById(@PathVariable int patientId){
		return patientService.getPatientById(patientId);
	}
	
	@PreAuthorize("hasAnyRole('RECEPTIONIST','ADMIN','PATIENT','SUPER_ADMIN')")
	@DeleteMapping("/{patientId}")
	public ResponseEntity<ResponseStructure<Patient>>  deletePatientById(@PathVariable int patientId){
		return patientService.deletePatientById(patientId);
	}
	
	@PreAuthorize("hasAnyRole('RECEPTIONIST','ADMIN','PATIENT','SUPER_ADMIN')")
	@PutMapping("/{patientId}")
	public ResponseEntity<ResponseStructure<Patient>> updatePatient(@PathVariable int patientId,@RequestBody Patient patient){
		return patientService.updatePatient(patientId, patient);
	}
	
	@GetMapping("/appointments")
	@PreAuthorize("hasAnyRole('RECEPTIONIST','ADMIN','PATIENT','SUPER_ADMIN')")
	public ResponseEntity<ResponseStructure<List<Appointment>>> getAppointmentsForLoggedInPatient() {
	    return patientService.getLoggedInPatientAppointments();
	}

	@PostMapping("/upload")
	@PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
	public ResponseEntity<ResponseStructure<String>> uploadCSV(@RequestParam("file") MultipartFile file) {
	    try {
	        return patientService.saveBulkPatients(file);
	    } catch (Exception e) {
	        ResponseStructure<String> responseStructure = new ResponseStructure<>();
	        responseStructure.setStatuscode(HttpStatus.INTERNAL_SERVER_ERROR.value());
	        responseStructure.setMessage("Upload failed");
	        responseStructure.setData("❌ Error: " + e.getMessage());

	        return new ResponseEntity<>(responseStructure, HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}


}
