package com.example.demo.serviceImpl;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.demo.ennums.AppointmentStatus;
import com.example.demo.ennums.Role;
import com.example.demo.entity.Appointment;
import com.example.demo.entity.Doctor;
import com.example.demo.entity.Patient;
import com.example.demo.entity.User;
import com.example.demo.exception.NoDataFoundInDatabaseException;
import com.example.demo.repository.AppointmentRepository;
import com.example.demo.repository.DoctorRepository;
import com.example.demo.repository.PatientRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.AppointmentService;
import com.example.demo.utility.ResponseStructure;

@Service
public class ApppointmentServiceImpl implements AppointmentService {

    private static final Logger logger = LoggerFactory.getLogger(ApppointmentServiceImpl.class);

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private UserRepository userRepository;
   

    @Override
    public ResponseEntity<ResponseStructure<Appointment>> saveAppointment(Appointment appointment) {
        int doctorId = (appointment.getDoctor() != null) ? appointment.getDoctor().getDoctorId() : 0;
        int patientId = (appointment.getPatient() != null) ? appointment.getPatient().getPatientId() : 0;

        logger.info("Saving appointment: doctorId={}, patientId={}", doctorId, patientId);

        if (doctorId == 0 || patientId == 0) {
            logger.error("Doctor ID or Patient ID is missing");
            throw new IllegalArgumentException("Doctor ID and Patient ID must not be null or 0");
        }

       
        Doctor doctor = doctorRepository.findById(doctorId)
            .orElseThrow(() -> {
                logger.error("Doctor not found with ID: {}", doctorId);
                return new NoDataFoundInDatabaseException("Doctor not found with ID: " + doctorId);
            });

    
        Patient patient = patientRepository.findById(patientId)
            .orElseThrow(() -> {
                logger.error("Patient not found with ID: {}", patientId);
                return new NoDataFoundInDatabaseException("Patient not found with ID: " + patientId);
            });

   
        if (appointment.getAppointmentDate() == null || appointment.getStartTime() == null || appointment.getEndTime() == null) {
            logger.error("Appointment date or time is missing");
            throw new IllegalArgumentException("Appointment date, start time, and end time must not be null");
        }

        LocalDate date = appointment.getAppointmentDate();
        LocalTime startTime = appointment.getStartTime();
        LocalTime endTime = appointment.getEndTime();

        if (endTime.isBefore(startTime) || endTime.equals(startTime)) {
            logger.error("Invalid time slot: start={}, end={}", startTime, endTime);
            throw new IllegalArgumentException("End time must be after start time");
        }

        boolean overlapExists = appointmentRepository.existsOverlappingAppointment(doctorId, date, startTime, endTime);
        if (overlapExists) {
            logger.error("Appointment overlaps with an existing one for doctorId={} on date={}", doctorId, date);
            throw new IllegalStateException("Selected time slot is already booked. Please choose another slot.");
        }

        appointment.setDoctor(doctor);
        appointment.setPatient(patient);
        appointment.setStatus(AppointmentStatus.APPROVED); 

        Appointment savedAppointment = appointmentRepository.save(appointment);

        logger.info("Appointment saved successfully with ID: {}", savedAppointment.getAppointmentId());

        ResponseStructure<Appointment> response = new ResponseStructure<>();
        response.setStatuscode(HttpStatus.CREATED.value());
        response.setMessage("Appointment saved successfully");
        response.setData(savedAppointment);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


    @Override
    public ResponseEntity<ResponseStructure<Appointment>> getAppointmentById(int appointmentId) {
        logger.info("Fetching appointment with ID: {}", appointmentId);

        Optional<Appointment> optional = appointmentRepository.findById(appointmentId);

        if (optional.isEmpty()) {
            logger.warn("Appointment not found with ID: {}", appointmentId);
            throw new NoDataFoundInDatabaseException("Appointment Not Found");
        }

        Appointment appointment = optional.get();
        logger.info("Appointment found: {}", appointment);

        ResponseStructure<Appointment> responseStructure = new ResponseStructure<>();
        responseStructure.setStatuscode(HttpStatus.OK.value());
        responseStructure.setMessage("Appointment object found by ID");
        responseStructure.setData(appointment);

        return new ResponseEntity<>(responseStructure, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ResponseStructure<List<Appointment>>> getAllAppointments() {
        logger.info("Fetching all appointments...");

        List<Appointment> appointments = appointmentRepository.findAllAppointmentsWithDetails();

        if (appointments.isEmpty()) {
            logger.warn("No appointment data found");
            throw new NoDataFoundInDatabaseException("No Appointment Data Found");
        }

        logger.info("Total appointments found: {}", appointments.size());

        ResponseStructure<List<Appointment>> responseStructure = new ResponseStructure<>();
        responseStructure.setStatuscode(HttpStatus.OK.value());
        responseStructure.setMessage("All Appointment objects found");
        responseStructure.setData(appointments);

        return new ResponseEntity<>(responseStructure, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ResponseStructure<Appointment>> deleteAppointmentById(int appointmentId) {
        logger.info("Attempting to delete appointment with ID: {}", appointmentId);

        Optional<Appointment> optional = appointmentRepository.findById(appointmentId);

        if (optional.isEmpty()) {
            logger.warn("Appointment not found for deletion with ID: {}", appointmentId);
            throw new NoDataFoundInDatabaseException("Appointment Not Found");
        }

        Appointment existingAppointment = optional.get();
        appointmentRepository.delete(existingAppointment);

        logger.info("Appointment deleted successfully with ID: {}", appointmentId);

        ResponseStructure<Appointment> responseStructure = new ResponseStructure<>();
        responseStructure.setStatuscode(HttpStatus.OK.value());
        responseStructure.setMessage("Appointment deleted successfully");
        responseStructure.setData(existingAppointment);

        return new ResponseEntity<>(responseStructure, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ResponseStructure<Appointment>> updateAppointment(int appointmentId, Appointment updatedAppointment) {
        logger.info("Updating appointment with ID: {}", appointmentId);

        Optional<Appointment> optional = appointmentRepository.findById(appointmentId);

        if (optional.isEmpty()) {
            logger.warn("Appointment not found for update with ID: {}", appointmentId);
            throw new NoDataFoundInDatabaseException("Appointment Not Found");
        }

        Appointment existingAppointment = optional.get();

        existingAppointment.setAppointmentDate(updatedAppointment.getAppointmentDate());
        existingAppointment.setAppointmentTime(updatedAppointment.getAppointmentTime());
        existingAppointment.setReason(updatedAppointment.getReason());

        Appointment savedAppointment = appointmentRepository.save(existingAppointment);

        logger.info("Appointment updated successfully: {}", savedAppointment);

        ResponseStructure<Appointment> responseStructure = new ResponseStructure<>();
        responseStructure.setStatuscode(HttpStatus.OK.value());
        responseStructure.setMessage("Appointment updated successfully");
        responseStructure.setData(savedAppointment);

        return new ResponseEntity<>(responseStructure, HttpStatus.OK);
    }

    @Override
    public List<Appointment> getAppointmentsByDoctor(Doctor doctor) {
        logger.info("Fetching appointments for doctor: {}", doctor.getDoctorName());
        return appointmentRepository.findByDoctor(doctor);
    }
    @Override
    public ResponseEntity<ResponseStructure<Appointment>> updateAppointmentStatus(int appointmentId, AppointmentStatus status) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        appointment.setStatus(status);   

        Appointment updated = appointmentRepository.save(appointment);

        ResponseStructure<Appointment> structure = new ResponseStructure<>();
        structure.setStatuscode(HttpStatus.OK.value());
        structure.setMessage("Appointment status updated to " + status);
        structure.setData(updated);

        return ResponseEntity.ok(structure);
    }


    @Override
    public Appointment cancelAppointment(int appointmentId, int loggedInUserId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found with ID: " + appointmentId));

        // Load the logged-in user
        User loggedUser = userRepository.findById(loggedInUserId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        boolean canCancel = false;

        // Patient can cancel only their own appointment
        if (loggedUser.getRole().equals(Role.PATIENT)) {
            if (appointment.getPatient() != null &&
                appointment.getPatient().getUser() != null &&
                appointment.getPatient().getUser().getUserId() == loggedInUserId) {
                canCancel = true;
            }
        }

        // Admin or SuperAdmin can cancel any appointment
        if (loggedUser.getRole().equals(Role.ADMIN) || loggedUser.getRole().equals(Role.SUPER_ADMIN)) {
            canCancel = true;
        }

        if (!canCancel) {
            throw new RuntimeException("You are not authorized to cancel this appointment");
        }

        // Already cancelled?
        if (appointment.getStatus() == AppointmentStatus.CANCELLED) {
            throw new RuntimeException("Appointment is already cancelled");
        }

        // Mark cancelled
        appointment.setStatus(AppointmentStatus.CANCELLED);

        // Save and return
        return appointmentRepository.save(appointment);
    }



}
