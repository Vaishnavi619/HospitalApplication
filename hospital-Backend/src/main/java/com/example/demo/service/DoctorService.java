package com.example.demo.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.example.demo.dto.DoctorDto;
import com.example.demo.dto.DoctorRegistrationResponse;

import com.example.demo.entity.Doctor;

import com.example.demo.utility.ResponseStructure;

public interface DoctorService {

	public ResponseEntity<ResponseStructure<DoctorRegistrationResponse>> saveDoctor(DoctorDto doctorDto);
	public  ResponseEntity<ResponseStructure<List<Doctor>>> getAllDoctors();
	public ResponseEntity<ResponseStructure<Doctor>> getDoctorById(int doctorId);
	public ResponseEntity<ResponseStructure<Doctor>>  deleteDoctorById(int doctorId);
	public ResponseEntity<ResponseStructure<Doctor>>  updateDoctor(int doctorId,Doctor doctor);
	Doctor getLoggedInDoctor();
	


}
