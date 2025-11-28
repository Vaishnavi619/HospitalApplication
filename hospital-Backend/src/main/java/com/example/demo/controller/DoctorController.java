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

import com.example.demo.dto.DoctorDto;
import com.example.demo.dto.DoctorRegistrationResponse;

import com.example.demo.entity.Doctor;

import com.example.demo.service.DoctorService;
import com.example.demo.utility.ResponseStructure;

@RestController
@RequestMapping("/api/doctors")
@CrossOrigin(origins = "http://localhost:4200")
public class DoctorController {

    private final DoctorService doctorService;

    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }
	
	@PreAuthorize("hasAnyRole('ADMIN','RECEPTIONIST','DOCTOR','SUPER_ADMIN')")
	@PostMapping
	public ResponseEntity<ResponseStructure<DoctorRegistrationResponse>> saveDoctor(@RequestBody DoctorDto doctorDto){
		return doctorService.saveDoctor(doctorDto);
	}
	@PreAuthorize("hasAnyRole('ADMIN','RECEPTIONIST','DOCTOR','SUPER_ADMIN')")
	@GetMapping
	public  ResponseEntity<ResponseStructure<List<Doctor>>> getAllDoctors(){
		return doctorService.getAllDoctors();
	}
	
	@PreAuthorize("hasAnyRole('ADMIN','RECEPTIONIST','DOCTOR','SUPER_ADMIN')")
	@GetMapping("/{doctorId}")
	public ResponseEntity<ResponseStructure<Doctor>> getDoctorById(@PathVariable int doctorId){
		return doctorService.getDoctorById(doctorId);
	}
	
	@PreAuthorize("hasAnyRole('ADMIN','RECEPTIONIST','DOCTOR','SUPER_ADMIN')")
	@DeleteMapping("/{doctorId}")
	public ResponseEntity<ResponseStructure<Doctor>> deleteDoctorById(@PathVariable int doctorId){
		return doctorService.deleteDoctorById(doctorId);
	}
	
	@PreAuthorize("hasAnyRole('ADMIN','RECEPTIONIST','DOCTOR','SUPER_ADMIN')")
	@PutMapping("/{doctorId}")
	public ResponseEntity<ResponseStructure<Doctor>> updateDoctor(@PathVariable("doctorId") int doctorId,@RequestBody Doctor doctor){
		return doctorService.updateDoctor(doctorId, doctor);
	}
	

}
