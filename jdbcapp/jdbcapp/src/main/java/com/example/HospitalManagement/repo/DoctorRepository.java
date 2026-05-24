package com.example.HospitalManagement.repo;

import com.example.HospitalManagement.model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {

    List<Doctor> findByStateContainingIgnoreCaseAndSpecialityContainingIgnoreCase(
            String state,
            String speciality
    );
}

