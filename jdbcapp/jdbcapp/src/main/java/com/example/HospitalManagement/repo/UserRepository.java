package com.example.HospitalManagement.repo;

import com.example.HospitalManagement.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUniqueId(String uniqueId);

    boolean existsByUniqueId(String uniqueId);
}
