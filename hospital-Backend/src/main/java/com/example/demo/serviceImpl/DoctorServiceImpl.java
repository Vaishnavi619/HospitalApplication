package com.example.demo.serviceImpl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.dto.DoctorDto;
import com.example.demo.dto.DoctorRegistrationResponse;
import com.example.demo.ennums.Role;
import com.example.demo.entity.Appointment;
import com.example.demo.entity.Doctor;
import com.example.demo.entity.User;
import com.example.demo.exception.DoctorDeletionException;
import com.example.demo.exception.NoDataFoundInDatabaseException;
import com.example.demo.exception.UserNotFoundException;
import com.example.demo.jwt.config.JwtService;
import com.example.demo.repository.AppointmentRepository;
import com.example.demo.repository.DoctorRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.DoctorService;
import com.example.demo.utility.ResponseStructure;
import com.example.demo.utility.UserDoctorLinker;

@Service
public class DoctorServiceImpl implements DoctorService {
	
	@Autowired
	private DoctorRepository doctorRepository;

	@Autowired
	private AppointmentRepository appointmentRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private UserDoctorLinker userDoctorLinker;

	@Override
	public ResponseEntity<ResponseStructure<DoctorRegistrationResponse>> saveDoctor(DoctorDto doctorDto) {

	    Doctor doctor = new Doctor();
	    doctor.setDoctorName(doctorDto.getDoctorName());
	    doctor.setExperience(doctorDto.getExperience());
	    doctor.setSpecialization(doctorDto.getSpecialization());
	    doctor.setTimings(doctorDto.getTimings());
	    doctor.setEmail(doctorDto.getEmail()); 
	 
	    User user = userDoctorLinker.generateAndLinkUserForDoctor(doctor.getDoctorName());


	    user.setEmail(doctorDto.getEmail()); 
	    doctor.setUser(user);               

	    
	    Doctor savedDoctor = doctorRepository.save(doctor);

	
	    DoctorRegistrationResponse registrationResponse = new DoctorRegistrationResponse();
	    registrationResponse.setDoctor(savedDoctor);
	    registrationResponse.setUsername(user.getUsername());
	    registrationResponse.setPassword(doctor.getDoctorName().replaceAll("[^a-zA-Z]", "").toLowerCase() + "123");

	
	    ResponseStructure<DoctorRegistrationResponse> responseStructure = new ResponseStructure<>();
	    responseStructure.setStatuscode(HttpStatus.OK.value());
	    responseStructure.setMessage("✅ Doctor Registered Successfully with Login Credentials");
	    responseStructure.setData(registrationResponse);

	    return new ResponseEntity<>(responseStructure, HttpStatus.OK);
	}



	@Override
	public ResponseEntity<ResponseStructure<List<Doctor>>> getAllDoctors() {
		List<Doctor> doctors=doctorRepository.findAll();
		if(doctors.isEmpty()) {
			throw new NoDataFoundInDatabaseException("No data Found");
		}else {
			ResponseStructure<List<Doctor>> responseStructure= new ResponseStructure<>();
			responseStructure.setStatuscode(HttpStatus.OK.value());
			responseStructure.setMessage("All Doctor objects found");
			responseStructure.setData(doctors);
			return new ResponseEntity<ResponseStructure<List<Doctor>>>(responseStructure,HttpStatus.OK);
		}
	}
	@Override
	public ResponseEntity<ResponseStructure<Doctor>> getDoctorById(int doctorId) {
		Optional<Doctor> optional=doctorRepository.findById(doctorId);
		if(optional.isEmpty()) {
			throw new UserNotFoundException("Doctor Not Found");
		}else {
			Doctor doctor=optional.get();
			ResponseStructure<Doctor> responseStructure= new ResponseStructure<Doctor>();
			responseStructure.setStatuscode(HttpStatus.OK.value());
			responseStructure.setMessage("Doctor object found by Id");
			responseStructure.setData(doctor);
			return new ResponseEntity<ResponseStructure<Doctor>>(responseStructure, HttpStatus.OK);
		}
	}
	@Override
	public ResponseEntity<ResponseStructure<Doctor>> deleteDoctorById(int doctorId) {
	    Optional<Doctor> optional = doctorRepository.findById(doctorId);

	    if (optional.isEmpty()) {
	        throw new UserNotFoundException("Doctor Not Found");
	    }

	    boolean hasAppointments = appointmentRepository.existsByDoctorDoctorId(doctorId);

	    if (hasAppointments) {
	        throw new DoctorDeletionException("Cannot delete doctor: Doctor is associated with one or more appointments.");
	    }

	    Doctor existingDoctor = optional.get();
	    doctorRepository.delete(existingDoctor);

	    ResponseStructure<Doctor> responseStructure = new ResponseStructure<>();
	    responseStructure.setStatuscode(HttpStatus.OK.value());
	    responseStructure.setMessage("Doctor object deleted by Id");
	    responseStructure.setData(existingDoctor);

	    return new ResponseEntity<>(responseStructure, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<ResponseStructure<Doctor>> updateDoctor( int doctorId, Doctor updatedDoctor) {
		Optional<Doctor> optional=doctorRepository.findById(doctorId);
		if(optional.isEmpty()) {
			return null;
		}else {
			Doctor existingDoctor=optional.get();
			updatedDoctor.setDoctorId(existingDoctor.getDoctorId());
			ResponseStructure<Doctor> responseStructure= new ResponseStructure<>();
			responseStructure.setStatuscode(HttpStatus.OK.value());
			responseStructure.setMessage("Doctor object updated by Id");
			responseStructure.setData(updatedDoctor);
			doctorRepository.save(updatedDoctor);
			return new ResponseEntity<ResponseStructure<Doctor>>(responseStructure, HttpStatus.OK);
			
		}
		
	}
	
	@Override
	public Doctor getLoggedInDoctor() {
	    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    String username = auth.getName(); // gets username from token

	    User user = userRepository.findByUsername(username)
	        .orElseThrow(() -> new UsernameNotFoundException("User not found"));

	    return doctorRepository.findByUser(user)
	        .orElseThrow(() -> new RuntimeException("Doctor not found for this user"));
	}

	

}
