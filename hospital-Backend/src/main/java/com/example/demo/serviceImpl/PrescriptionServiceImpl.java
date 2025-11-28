package com.example.demo.serviceImpl;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.demo.dto.MedicineLineDto;
import com.example.demo.dto.PrescriptionDto;
import com.example.demo.entity.Appointment;
import com.example.demo.entity.Doctor;
import com.example.demo.entity.Medicine;
import com.example.demo.entity.MedicineEntry;
import com.example.demo.entity.Patient;
import com.example.demo.entity.Prescription;
import com.example.demo.entity.User;
import com.example.demo.exception.NoDataFoundInDatabaseException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.exception.UserNotFoundException;
import com.example.demo.repository.AppointmentRepository;
import com.example.demo.repository.DoctorRepository;
import com.example.demo.repository.MedicineRepository;
import com.example.demo.repository.PatientRepository;
import com.example.demo.repository.PrescriptionRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.DoctorService;
import com.example.demo.service.PatientService;
import com.example.demo.service.PrescriptionService;
import com.example.demo.utility.ResponseStructure;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class PrescriptionServiceImpl implements PrescriptionService{

	@Autowired
	private  PrescriptionRepository prescriptionRepository;
	@Autowired
	private  AppointmentRepository appointmentRepository;
	@Autowired
	private MedicineRepository medicineRepository;
	@Autowired
	 private DoctorRepository doctorRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private DoctorService doctorService;
	@Autowired
	private PatientService patientService;
	@Autowired
	private PatientRepository patientRepository;
	@Override
	public ResponseEntity<ResponseStructure<Prescription>> savePrescription(int appointmentId, PrescriptionDto prescriptionDto) {
	    // ✅ Validate input
	    if (prescriptionDto == null) {
	        throw new IllegalArgumentException("Prescription data is missing");
	    }

	    if (prescriptionDto.getDiagnosis() == null || prescriptionDto.getDiagnosis().isEmpty()) {
	        throw new IllegalArgumentException("Diagnosis is required");
	    }

	    if (prescriptionDto.getMedicines() == null || prescriptionDto.getMedicines().isEmpty()) {
	        throw new IllegalArgumentException("At least one medicine must be provided");
	    }

	    // ✅ Fetch appointment
	    Appointment appointment = appointmentRepository.findById(appointmentId)
	            .orElseThrow(() -> new ResourceNotFoundException("Appointment not found with ID: " + appointmentId));

	    // ✅ Get logged-in doctor
	    Doctor doctor = getLoggedInDoctor();
	    if (doctor == null) {
	        throw new UserNotFoundException("Logged-in doctor not found");
	    }

	    // ✅ Create new Prescription
	    Prescription prescription = new Prescription();
	    prescription.setDiagnosis(prescriptionDto.getDiagnosis());
	    prescription.setDate(prescriptionDto.getDate() != null ? prescriptionDto.getDate() : LocalDate.now());
	    prescription.setAppointment(appointment);
	    prescription.setDoctor(doctor);
	    prescription.setPatient(appointment.getPatient());

	    // ✅ Fill medicine data and convert to JSON
	    List<MedicineLineDto> medList = prescriptionDto.getMedicines();

	    for (MedicineLineDto medDto : medList) {
	        if (medDto.getMedicineId() != null) {
	            Medicine medicine = medicineRepository.findById(medDto.getMedicineId())
	                    .orElseThrow(() -> new ResourceNotFoundException("Medicine not found with ID: " + medDto.getMedicineId()));
	            medDto.setMedicineName(medicine.getName());
	            medDto.setPrice(medicine.getPrice());
	        }
	    }

	    try {
	        // Convert medicine list to JSON string
	        ObjectMapper mapper = new ObjectMapper();
	        String medicinesJson = mapper.writeValueAsString(medList);
	        prescription.setMedicinesJson(medicinesJson);
	    } catch (Exception e) {
	        throw new RuntimeException("Error converting medicines to JSON: " + e.getMessage());
	    }

	    // ✅ Save prescription
	    Prescription savedPrescription = prescriptionRepository.save(prescription);

	    // ✅ Prepare response
	    ResponseStructure<Prescription> response = new ResponseStructure<>();
	    response.setStatuscode(HttpStatus.OK.value());
	    response.setMessage("Prescription created successfully");
	    response.setData(savedPrescription);

	    return new ResponseEntity<>(response, HttpStatus.OK);
	}



	 @Override
	    public ResponseEntity<ResponseStructure<List<Prescription>>> getAllPrescriptions() {
	        List<Prescription> prescriptions = prescriptionRepository.findAll();

	        if (prescriptions.isEmpty()) {
	            throw new NoDataFoundInDatabaseException("No Prescription Data Found");
	        } else {
	            ResponseStructure<List<Prescription>> responseStructure = new ResponseStructure<>();
	            responseStructure.setStatuscode(HttpStatus.OK.value());
	            responseStructure.setMessage("All prescriptions retrieved successfully");
	            responseStructure.setData(prescriptions);

	            return new ResponseEntity<>(responseStructure, HttpStatus.OK);
	        }
	    }


	 @Override
	    public ResponseEntity<ResponseStructure<Prescription>> getPrescriptionById(int prescriptionId) {
	        Prescription prescription = prescriptionRepository.findById(prescriptionId)
	                .orElseThrow(() -> new NoDataFoundInDatabaseException("Prescription Not Found"));

	        ResponseStructure<Prescription> response = new ResponseStructure<>();
	        response.setStatuscode(HttpStatus.OK.value());
	        response.setMessage("Prescription retrieved successfully");
	        response.setData(prescription);

	        return new ResponseEntity<>(response, HttpStatus.OK);
	    }

	 @Override
	    public ResponseEntity<ResponseStructure<Prescription>> deletePrescription(int prescriptionId) {
	        Prescription prescription = prescriptionRepository.findById(prescriptionId)
	                .orElseThrow(() -> new NoDataFoundInDatabaseException("Prescription Not Found"));

	        prescriptionRepository.delete(prescription);

	        ResponseStructure<Prescription> response = new ResponseStructure<>();
	        response.setStatuscode(HttpStatus.OK.value());
	        response.setMessage("Prescription deleted successfully");
	        response.setData(prescription);

	        return new ResponseEntity<>(response, HttpStatus.OK);
	    }


	 @Override
	 public ResponseEntity<ResponseStructure<Prescription>> updatePrescription(int prescriptionId, Prescription updatedPrescription) {
	     // ✅ 1. Fetch existing prescription
	     Prescription existingPrescription = prescriptionRepository.findById(prescriptionId)
	             .orElseThrow(() -> new NoDataFoundInDatabaseException("Prescription Not Found"));

	     // ✅ 2. Update simple fields
	     existingPrescription.setDiagnosis(updatedPrescription.getDiagnosis());
	     existingPrescription.setDate(updatedPrescription.getDate());

	     // ✅ 3. Serialize medicines list into JSON string
	     if (updatedPrescription.getMedicinesJson() != null && !updatedPrescription.getMedicinesJson().isEmpty()) {
	         existingPrescription.setMedicinesJson(updatedPrescription.getMedicinesJson());
	     } else if (updatedPrescription.getMedicinesJson() != null) {
	         // if frontend sends as object instead of string, serialize it
	         try {
	             ObjectMapper mapper = new ObjectMapper();
	             String json = mapper.writeValueAsString(updatedPrescription.getMedicinesJson());
	             existingPrescription.setMedicinesJson(json);
	         } catch (Exception e) {
	             throw new RuntimeException("Failed to serialize updated medicines list");
	         }
	     }

	     // ✅ 4. Save updated record
	     Prescription savedPrescription = prescriptionRepository.save(existingPrescription);

	     // ✅ 5. Prepare response
	     ResponseStructure<Prescription> response = new ResponseStructure<>();
	     response.setStatuscode(HttpStatus.OK.value());
	     response.setMessage("Prescription updated successfully");
	     response.setData(savedPrescription);

	     return new ResponseEntity<>(response, HttpStatus.OK);
	 }


	 @Override
	    public Doctor getLoggedInDoctor() {
	        String username = SecurityContextHolder.getContext().getAuthentication().getName();
	        User user = userRepository.findByUsername(username)
	                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
	        return doctorRepository.findByUser(user)
	                .orElseThrow(() -> new RuntimeException("Doctor not found"));
	    }
	 @Override
	    public ResponseEntity<ResponseStructure<List<Prescription>>> getPrescriptionsForLoggedInDoctor() {
	        Doctor doctor = doctorService.getLoggedInDoctor();
	        List<Prescription> prescriptions = prescriptionRepository.findByAppointmentDoctor(doctor);

	        ResponseStructure<List<Prescription>> response = new ResponseStructure<>();
	        response.setStatuscode(HttpStatus.OK.value());
	        response.setMessage("Prescriptions for logged-in doctor retrieved successfully");
	        response.setData(prescriptions);

	        return new ResponseEntity<>(response, HttpStatus.OK);
	    }

	 @Override
	    public ResponseEntity<ResponseStructure<List<Prescription>>> getPrescriptionsForLoggedInPatient() {
	        ResponseEntity<ResponseStructure<Patient>> patientResponse = patientService.getLoggedInPatient();
	        Patient patient = patientResponse.getBody().getData();

	        List<Prescription> prescriptions = prescriptionRepository.findByAppointmentPatient(patient);

	        ResponseStructure<List<Prescription>> response = new ResponseStructure<>();
	        response.setStatuscode(HttpStatus.OK.value());
	        response.setMessage("Prescriptions for logged-in patient retrieved successfully");
	        response.setData(prescriptions);

	        return new ResponseEntity<>(response, HttpStatus.OK);
	    }
	 
	 @Override
	 public ResponseEntity<ResponseStructure<List<Prescription>>> getPrescriptionsByPatient(int patientId) {
	     Patient p = patientRepository.findById(patientId)
	            .orElseThrow(() -> new UserNotFoundException("Patient not found"));
	     List<Prescription> list = prescriptionRepository.findByPatient(p); // create repo method
	     ResponseStructure<List<Prescription>> resp = new ResponseStructure<>();
	     resp.setStatuscode(HttpStatus.OK.value());
	     resp.setMessage("Prescriptions fetched");
	     resp.setData(list);
	     return new ResponseEntity<>(resp, HttpStatus.OK);
	 }

}