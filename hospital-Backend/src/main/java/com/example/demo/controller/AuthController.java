package com.example.demo.controller;

import com.example.demo.exception.EmailNotRegisteredException;
import com.example.demo.exception.UserNotFoundException;
import com.example.demo.jwt.config.JwtService;

import com.example.demo.entity.Doctor;
import com.example.demo.entity.OtpVerification;
import com.example.demo.entity.Patient;
import com.example.demo.entity.User;
import com.example.demo.repository.DoctorRepository;
import com.example.demo.repository.OtpVerificationRepository;
import com.example.demo.repository.PatientRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.EmailService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:4200")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    private static final Random RANDOM = new Random();  // ♻ Reuse Random

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final OtpVerificationRepository otpVerificationRepository;
    private final EmailService emailService;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;

    public AuthController(
            JwtService jwtService,
            UserRepository userRepository,
            OtpVerificationRepository otpVerificationRepository,
            EmailService emailService,
            PatientRepository patientRepository,
            DoctorRepository doctorRepository
    ) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.otpVerificationRepository = otpVerificationRepository;
        this.emailService = emailService;
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
    }


    // ========================= LOGIN =========================

    @PostMapping("/login")
    public ResponseEntity<?> loginWithEmail(@RequestBody Map<String, String> request) {

        String email = request.get("email");

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EmailNotRegisteredException("Email not registered"));

        String otp = String.valueOf(RANDOM.nextInt(900000) + 100000);

        logger.info("Generated OTP for {}: {}", email, otp);

        OtpVerification otpVerification = otpVerificationRepository
                .findByEmail(email)
                .orElse(new OtpVerification());

        otpVerification.setEmail(email);
        otpVerification.setOtp(otp);
        otpVerification.setTimestamp(LocalDateTime.now());
        otpVerificationRepository.save(otpVerification);

        emailService.sendOtpEmail(email, otp);

        Map<String, String> response = new HashMap<>();
        response.put("message", "OTP has been sent to your email address.");
        return ResponseEntity.ok(response);
    }


    // ========================= VERIFY OTP =========================

    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestBody Map<String, String> request) {

        String email = request.get("email");
        String otp = request.get("otp");

        Optional<OtpVerification> otpEntry = otpVerificationRepository.findByEmail(email);

        if (otpEntry.isEmpty()) {
            return ResponseEntity.status(404).body("No OTP generated for this email.");
        }

        if (!otpEntry.get().getOtp().equals(otp)) {
            return ResponseEntity.status(401).body("Incorrect OTP. Please try again.");
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found."));

        String token = jwtService.generateToken(user);

        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("role", user.getRole().name());

        if (user.getRole().name().equals("PATIENT")) {
            patientRepository.findByUser(user)
                    .ifPresent(patient -> response.put("patientId", patient.getPatientId()));
        }

        if (user.getRole().name().equals("DOCTOR")) {
            doctorRepository.findByUser(user)
                    .ifPresent(doctor -> response.put("doctorId", doctor.getDoctorId()));
        }

        return ResponseEntity.ok(response);
    }
}
