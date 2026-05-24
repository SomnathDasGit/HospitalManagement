package com.example.HospitalManagement.repo;


import com.example.HospitalManagement.model.MedicalReport;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MedicalReportRepository extends JpaRepository<MedicalReport, Long> {

    List<MedicalReport> findByPatientId(Long patientId);
}
