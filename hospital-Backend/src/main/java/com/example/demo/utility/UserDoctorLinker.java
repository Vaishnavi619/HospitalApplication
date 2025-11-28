package com.example.demo.utility;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.demo.ennums.Role;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserDoctorLinker {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User generateAndLinkUserForDoctor(String doctorName) {
        String baseUsername = doctorName.replaceAll("[^a-zA-Z]", "").toLowerCase();
        String username = generateUniqueUsername(baseUsername);
        String password = baseUsername + "123";

        String email = username + "@gmail.com"; 

        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(Role.DOCTOR);
        user.setEmail(email);

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
