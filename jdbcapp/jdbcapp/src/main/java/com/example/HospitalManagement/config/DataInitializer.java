package com.example.HospitalManagement.config;


import com.example.HospitalManagement.model.Role;
import com.example.HospitalManagement.model.User;
import com.example.HospitalManagement.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

@Configuration
@RequiredArgsConstructor

                            //CommandLineRunner runs automatically when the application starts.
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    //Executes immediately after Spring Boot finishes starting.
    public void run(String... args) {

        if (userRepository.count() == 0) {

            User admin = new User();
            admin.setUniqueId("ADMIN-" + UUID.randomUUID().toString().replace("-", "").substring(0, 10));
            //UUID.randomUUID().toString().replace("-", "").substring(0, 10)
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRole(Role.ADMIN);

            userRepository.save(admin);

            System.out.println("Default Admin Created");
            System.out.println("Admin ID: " + admin.getUniqueId());
            System.out.println("Password: admin123");
        }
    }
}
