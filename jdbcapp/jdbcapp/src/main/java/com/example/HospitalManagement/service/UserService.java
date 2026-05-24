package com.example.HospitalManagement.service;

import com.example.HospitalManagement.model.Role;
import com.example.HospitalManagement.model.User;
import com.example.HospitalManagement.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User createUser(String password, Role role) {

        User user = new User();
        user.setUniqueId("UID-" + UUID.randomUUID().toString().replace("-", "").substring(0, 10));
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(role);

        return userRepository.save(user);
    }

    public User createAdmin(User user, String password, Role role) {
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(role);
        user.setUniqueId("ADMIN-" + UUID.randomUUID().toString().replace("-", "").substring(0, 10));
        return userRepository.save(user);
    }

    public User findByUniqueId(String uniqueId) {
        return userRepository.findByUniqueId(uniqueId)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

}

