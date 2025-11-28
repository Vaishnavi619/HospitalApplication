package com.example.demo.controller;

import java.util.List;

import com.example.demo.exception.UnauthorizedException;
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
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.Appointment;
import com.example.demo.entity.Doctor;
import com.example.demo.jwt.config.JwtService;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.AppointmentService;
import com.example.demo.service.DoctorService;
import com.example.demo.service.PatientService;

import com.example.demo.utility.ResponseStructure;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/appointments")
@CrossOrigin(origins = "http://localhost:4200")
public class AppointmentController {
    private final AppointmentService appointmentService;
    private final PatientService patientService;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final DoctorService doctorService;


    public AppointmentController(
            AppointmentService appointmentService,
            PatientService patientService,
            JwtService jwtService,
            UserRepository userRepository,
            DoctorService doctorService
    ) {
        this.appointmentService = appointmentService;
        this.patientService = patientService;
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.doctorService = doctorService;
    }
	@PostMapping
	@PreAuthorize("hasAnyRole('RECEPTIONIST','ADMIN','PATIENT','SUPER_ADMIN','DOCTOR')")
	public ResponseEntity<ResponseStructure<Appointment>> saveAppointment(@RequestBody Appointment appointment){
		return appointmentService.saveAppointment(appointment);
	}

	@PreAuthorize("hasAnyRole('RECEPTIONIST','DOCTOR','PATIENT','SUPER_ADMIN','DOCTOR')")
	@GetMapping("/{appointmentId}")
	public ResponseEntity<ResponseStructure<Appointment>>  getAppointmentById(@PathVariable int appointmentId){
		return appointmentService.getAppointmentById(appointmentId);
	}

	@PreAuthorize("hasAnyRole('RECEPTIONIST','DOCTOR','PATIENT','SUPER_ADMIN','DOCTOR')")
	@GetMapping
	public ResponseEntity<ResponseStructure<List<Appointment>>> getAllAppointments(){
		return appointmentService.getAllAppointments();
	}

	@PreAuthorize("hasAnyRole('RECEPTIONIST','DOCTOR','PATIENT','SUPER_ADMIN','DOCTOR')")
	@DeleteMapping("/{appointmentId}")
	public ResponseEntity<ResponseStructure<Appointment>> deleteAppointmentById(@PathVariable int appointmentId){
		return appointmentService.deleteAppointmentById(appointmentId);
	}

	@PreAuthorize("hasAnyRole('RECEPTIONIST','DOCTOR','PATIENT','SUPER_ADMIN','DOCTOR')")
	@PutMapping("/{appointmentId}")
	public ResponseEntity<ResponseStructure<Appointment>> updateAppointment(@PathVariable int appointmentId,@RequestBody Appointment appointment){
		return appointmentService.updateAppointment(appointmentId, appointment);
	}
	@GetMapping("/patient/appointments")
	@PreAuthorize("hasAnyRole('RECEPTIONIST','DOCTOR','PATIENT','SUPER_ADMIN')")
	public ResponseEntity<ResponseStructure<List<Appointment>>> getAppointmentsForLoggedInPatient() {
	    return patientService.getLoggedInPatientAppointments(); // ✅ directly use service method
	}
	@PreAuthorize("hasAnyRole('RECEPTIONIST','DOCTOR','PATIENT','SUPER_ADMIN','DOCTOR')")
	@GetMapping("/doctor/appointments")
	public ResponseEntity<List<Appointment>> getAppointmentsForLoggedInDoctor() {
	    Doctor doctor = doctorService.getLoggedInDoctor();
	    List<Appointment> appointments = appointmentService.getAppointmentsByDoctor(doctor);
	    return ResponseEntity.ok(appointments);
	}
	private int getLoggedInUserIdFromRequest(HttpServletRequest request) {
	    String authHeader = request.getHeader("Authorization");
	    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new UnauthorizedException("Authorization header missing or invalid");

        }
	    String token = authHeader.substring(7); // remove "Bearer "
	    String username = jwtService.extractUsername(token);
	    return userRepository.findByUsername(username)
	            .orElseThrow(() -> new RuntimeException("Logged-in user not found"))
	            .getUserId();
	}
	@PostMapping("/{appointmentId}/cancel")
	public ResponseEntity<ResponseStructure<Appointment>> cancelAppointment(
	        @PathVariable int appointmentId,
	        HttpServletRequest request) {

	    int loggedInUserId = getLoggedInUserIdFromRequest(request);

	    Appointment cancelled = appointmentService.cancelAppointment(appointmentId, loggedInUserId);

	    ResponseStructure<Appointment> resp = new ResponseStructure<>();
	    resp.setStatuscode(HttpStatus.OK.value());
	    resp.setMessage("Appointment cancelled successfully");
	    resp.setData(cancelled);

	    return ResponseEntity.ok(resp);
	}


}
