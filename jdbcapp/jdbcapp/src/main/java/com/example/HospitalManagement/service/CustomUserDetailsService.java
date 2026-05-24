package com.example.HospitalManagement.service;

import com.example.HospitalManagement.model.User;
import com.example.HospitalManagement.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;


    @Override
    public UserDetails loadUserByUsername(String uniqueId)
            throws UsernameNotFoundException {

        // Find user by unique ID (for both admin & patient)
        Optional<User> optionalUser = userRepository.findByUniqueId(uniqueId);

        if (optionalUser.isEmpty()) {
            throw new UsernameNotFoundException("User not found with ID: " + uniqueId);
        }

        User user = optionalUser.get();

        // Map user to Spring Security User
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUniqueId())
                .password(user.getPassword())
                .roles(user.getRole().name()) // e.g., ADMIN or PATIENT
                .build();
    }

    /*
    @Override
    public UserDetails loadUserByUsername(String uniqueId) throws UsernameNotFoundException {

        Optional<User> optionalUser = userRepository.findByUniqueId(uniqueId);

        if (optionalUser.isEmpty()) {
            throw new UsernameNotFoundException("User not found with ID: " + uniqueId);
        }

        User user = optionalUser.get();

        // Return your CustomUserDetails wrapping your User entity
        return new CustomUserDetails(user);
    }

    */
}