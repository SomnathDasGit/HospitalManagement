package com.example.HospitalManagement.service;
import com.example.HospitalManagement.model.MedicalReport;
import com.example.HospitalManagement.model.Paitent;
import com.example.HospitalManagement.repo.MedicalReportRepository;
import com.example.HospitalManagement.repo.PaitentRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
public class MedicalReportService {

    private final MedicalReportRepository reportRepository;
    private final PaitentRepository patientRepository;

    // Path where uploaded files will be stored
    private final String uploadDir = System.getProperty("user.dir") + "/uploads/";

    public MedicalReportService(MedicalReportRepository reportRepository,
                                PaitentRepository patientRepository) {
        this.reportRepository = reportRepository;
        this.patientRepository = patientRepository;
    }

    // Upload report for a patient by ID
    public void uploadReport(Long patientId, MultipartFile file) throws IOException {

        // Find the patient
        Paitent patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        // Ensure upload directory exists
        File folder = new File(uploadDir);
        if (!folder.exists()) folder.mkdirs();

        // Validate file type (optional)
        String contentType = file.getContentType();
        if (!contentType.equals("application/pdf") &&
                !contentType.equals("image/jpeg") &&
                !contentType.equals("image/png")) {
            throw new RuntimeException("Only PDF, JPG, PNG allowed");
        }

        // Save file to disk
        String filePath = uploadDir + file.getOriginalFilename();
        file.transferTo(new File(filePath));

        // Save report in database
        MedicalReport report = new MedicalReport();
        report.setFileName(file.getOriginalFilename());
        report.setFileType(file.getContentType());
        report.setFilePath(filePath);
        report.setPatient(patient);
        report.setUploadDate(LocalDateTime.now());
        reportRepository.save(report);
    }


    public List<MedicalReport> getReportsByPatientId(Long patientId) {
        return reportRepository.findByPatientId(patientId);
    }


    public File getReportFile(Long reportId, Long patientId) {
        MedicalReport report = reportRepository.findById(reportId)
                .orElseThrow(() -> new RuntimeException("Report not found"));

        // Check if the report belongs to this patient
        if (!Objects.equals(report.getPatient().getId(), patientId)) {
            throw new RuntimeException("Access denied: report does not belong to this patient");
        }

        // Return the file object
        return new File(report.getFilePath());
    }


}