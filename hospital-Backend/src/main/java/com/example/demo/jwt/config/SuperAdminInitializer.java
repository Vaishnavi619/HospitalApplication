package com.example.demo.jwt.config;



import com.example.demo.ennums.Role;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class SuperAdminInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public SuperAdminInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
    	boolean exists = userRepository.existsByRole(Role.SUPER_ADMIN);

        if (!exists) {
            User superAdmin = new User();
            superAdmin.setUsername("Suriya");
            superAdmin.setEmail("suriya@gmail.com");
            superAdmin.setPassword(passwordEncoder.encode("suriya123"));
            superAdmin.setRole(Role.SUPER_ADMIN);

            userRepository.save(superAdmin);
            System.out.println("✅ Super Admin created with username: Suriya and password: suriya123");
        } else {
            System.out.println("✅ Super Admin already exists");
        }
    }
}

