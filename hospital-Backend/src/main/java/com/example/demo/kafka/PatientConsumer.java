package com.example.demo.kafka;
import com.example.demo.dto.PatientDto;
import com.example.demo.ennums.Role;
import com.example.demo.entity.Patient;

import com.example.demo.entity.User;
import com.example.demo.repository.PatientRepository;
import com.example.demo.repository.UserRepository;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class PatientConsumer {

    private final PatientRepository patientRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public PatientConsumer(PatientRepository patientRepository,
                          UserRepository userRepository,
                          PasswordEncoder passwordEncoder) {
        this.patientRepository = patientRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @KafkaListener(topics = "bulk-patient-topic", groupId = "patient-group", containerFactory = "patientKafkaListenerFactory")
    public void consume(PatientDto dto) {
        System.out.println("📥 Consuming patient from Kafka: " + dto.getFullName());

        Optional<Patient> existingPatient = patientRepository.findByPhone(dto.getPhone());
        if (existingPatient.isPresent()) {
            System.out.println("⚠️ Duplicate patient found with phone: " + dto.getPhone());
            return; 
        }

        Optional<User> existingUser = userRepository.findByEmail(dto.getEmail());
        if (existingUser.isPresent()) {
            System.out.println("⚠️ Duplicate user found with email: " + dto.getEmail());
            return; 
        }

        String baseUsername = dto.getFullName().replaceAll("[^a-zA-Z]", "").toLowerCase();
        String uniqueUsername = generateUniqueUsername(baseUsername);
        String password = baseUsername + "123";

        User user = new User();
        user.setUsername(uniqueUsername);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(Role.PATIENT);
        user.setEmail(dto.getEmail());

        userRepository.save(user);

        Patient patient = new Patient();
        patient.setFullName(dto.getFullName());
        patient.setAge(dto.getAge());
        patient.setGender(dto.getGender());
        patient.setPhone(dto.getPhone());
        patient.setAddress(dto.getAddress());
        patient.setRegisteredDate(dto.getRegisteredDate());
        patient.setUser(user); // 🔗 Link to user

        patientRepository.save(patient);

        System.out.println("✅ Patient saved: " + dto.getFullName());
    }


    private String generateUniqueUsername(String baseUsername) {
        String username = baseUsername;
        int count = 0;
        while (userRepository.findByUsername(username).isPresent()) {
            count++;
            username = baseUsername + count;
        }
        return username;
    }
}
