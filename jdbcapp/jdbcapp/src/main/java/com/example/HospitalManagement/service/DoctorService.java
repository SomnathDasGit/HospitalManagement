package com.example.HospitalManagement.service;
import com.example.HospitalManagement.model.Doctor;
import com.example.HospitalManagement.repo.DoctorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DoctorService {

    private final DoctorRepository doctorRepository;

    // Save Doctor
    public Doctor saveDoctor(Doctor doctor) {
        return doctorRepository.save(doctor);
    }

    // Get Doctor by ID
    public Doctor getDoctorById(Long id) {
        return doctorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));
    }

    // Update Doctor
    public Doctor updateDoctor(Doctor updatedDoctor) {

        Doctor existingDoctor = doctorRepository.findById(updatedDoctor.getId())
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        existingDoctor.setName(updatedDoctor.getName());
        existingDoctor.setQualification(updatedDoctor.getQualification());
        existingDoctor.setExperience(updatedDoctor.getExperience());
        existingDoctor.setSpeciality(updatedDoctor.getSpeciality());
        existingDoctor.setState(updatedDoctor.getState());

        return doctorRepository.save(existingDoctor);
    }

    // Delete Doctor
    public void deleteDoctor(Long id) {
        doctorRepository.deleteById(id);
    }

    // Find by location and specialty (Upcoming Story)


    // Get all doctors
    public List<Doctor> getAllDoctors() {
        return doctorRepository.findAll();
    }

    public List<Doctor> searchDoctors(String state, String speciality) {

        if ((state == null || state.isEmpty()) &&
                (speciality == null || speciality.isEmpty())) {

            return new ArrayList<>();
        }

        return doctorRepository
                .findByStateContainingIgnoreCaseAndSpecialityContainingIgnoreCase(
                        state == null ? "" : state,
                        speciality == null ? "" : speciality
                );
    }
}
