package com.example.demo.utility;

import com.example.demo.ennums.Role;
import com.example.demo.entity.Patient;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserPatientLinker {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    
    public User generateAndLinkUserForPatient(Patient patient, String email) {
     
        String cleanedName = patient.getFullName().replaceAll("[^a-zA-Z]", "").toLowerCase();
        String username = generateUniqueUsername(cleanedName);
        String password = cleanedName + "123";

    
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setEmail(email); 
        user.setRole(Role.PATIENT);

  
        return userRepository.save(user);
    }

    private String generateUniqueUsername(String baseUsername) {
        int count = 0;
        String username = baseUsername;
        while (userRepository.findByUsername(username).isPresent()) {
            count++;
            username = baseUsername + count; 
        }
        return username;
    }
}
