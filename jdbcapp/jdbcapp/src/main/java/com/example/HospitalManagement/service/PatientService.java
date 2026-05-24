package com.example.HospitalManagement.service;

import com.example.HospitalManagement.model.Paitent;
import com.example.HospitalManagement.model.User;
import com.example.HospitalManagement.repo.PaitentRepository;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PatientService {

    private final PaitentRepository patientRepository;

    @Value("${file.upload-dir}")
    private String uploadDir;

    public ByteArrayInputStream downloadPatientsExcel() {

        List<Paitent> patients = patientRepository.findAll();

        try (
                Workbook workbook = new XSSFWorkbook();
                ByteArrayOutputStream out = new ByteArrayOutputStream()
        ) {

            Sheet sheet = workbook.createSheet("Patients");

            // Header Style
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);

            CellStyle headerStyle = workbook.createCellStyle();
            headerStyle.setFont(headerFont);

            // DATE STYLE
            CellStyle dateStyle = workbook.createCellStyle();

            CreationHelper creationHelper =
                    workbook.getCreationHelper();

            dateStyle.setDataFormat(
                    creationHelper.createDataFormat()
                            .getFormat("dd-MM-yyyy")
            );

            // Header Row
            Row headerRow = sheet.createRow(0);

            String[] columns = {
                    "ID",
                    "First Name",
                    "Last Name",
                    "Address",
                    "Phone",
                    "Dob"
            };

            for (int i = 0; i < columns.length; i++) {

                Cell cell = headerRow.createCell(i);

                cell.setCellValue(columns[i]);

                cell.setCellStyle(headerStyle);
            }

            // Data Rows
            int rowIdx = 1;

            for (Paitent patient : patients) {

                Row row = sheet.createRow(rowIdx++);

                row.createCell(0).setCellValue(patient.getId());
                row.createCell(1).setCellValue(patient.getFirstName());
                row.createCell(2).setCellValue(patient.getLastName());
                row.createCell(3).setCellValue(patient.getAddress());
                row.createCell(4).setCellValue(patient.getMobileNumber());

                // DOB CELL
                Cell dobCell = row.createCell(5);

                dobCell.setCellValue(patient.getDob());

                // IMPORTANT
                dobCell.setCellStyle(dateStyle);
            }

            // Auto-size columns
            for (int i = 0; i < columns.length; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(out);

            return new ByteArrayInputStream(out.toByteArray());

        } catch (Exception e) {

            throw new RuntimeException(
                    "Failed to generate Excel file", e
            );
        }
    }



    // Add a new patient (generates unique ID)
    public Paitent addPatient(Paitent patient) {
        //patient.setUniqueId("PAT-" + UUID.randomUUID());
        //patient.setU
        return patientRepository.save(patient);
    }


    // Get patient by ID
    public Paitent getPatientById(Long id) {
        return patientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Patient not found"));
    }

    // Update existing patient
    // Update Patient
    public Paitent updatePatient(Paitent patient) {

        Paitent existingPatient = getPatientById(patient.getId());

        existingPatient.setFirstName(patient.getFirstName());
        existingPatient.setMiddleName(patient.getMiddleName());
        existingPatient.setLastName(patient.getLastName());
        existingPatient.setDob(patient.getDob());
        existingPatient.setAddress(patient.getAddress());
        existingPatient.setState(patient.getState());
        existingPatient.setCountry(patient.getCountry());
        existingPatient.setMobileNumber(patient.getMobileNumber());
        existingPatient.setRelativeName(patient.getRelativeName());
        existingPatient.setRelativeMobile(patient.getRelativeMobile());
        existingPatient.setIllnessDetails(patient.getIllnessDetails());
        existingPatient.setRequirement(patient.getRequirement());

        return patientRepository.save(existingPatient);
    }

    // Search patients by unique ID, first name, or last name




    /*
         * @param patient Patient entity
     * @param reportFile Multipart file uploaded by admin

    public void saveReport(Paitent patient, MultipartFile file) throws IOException {
        if (file.isEmpty()) return;

        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path filePath = Paths.get(uploadDir).resolve(fileName);

        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        patient.setReportFileName(fileName);
        patientRepository.save(patient);
    }

    public Paitent findByUser(User user) {
        return patientRepository.findByUser(user);
    }


    public PaitentService(PaitentRepository patientRepository) {
        this.patientRepository = patientRepository;
    }
*/

    public Paitent findByUser(User user) {
        return patientRepository.findByUser(user);
    }



/*

        public void PaitentService(PaitentRepository patientRepository) {
            this.patientRepository = patientRepository;
        }

        /**
         * Search patients by first name, last name, or uniqueId.
         * Any parameter can be null or empty, meaning "ignore this filter".
         */
        public List<Paitent> searchPatients(String firstName, String lastName, String uniqueId) {

            // Trim inputs
            if (firstName != null) firstName = firstName.trim();
            if (lastName != null) lastName = lastName.trim();
            if (uniqueId != null) uniqueId = uniqueId.trim();

            // If no search parameters, return empty list or all patients
            if ((firstName == null || firstName.isEmpty())
                    && (lastName == null || lastName.isEmpty())
                    && (uniqueId == null || uniqueId.isEmpty())) {
                return Collections.emptyList(); // or patientRepository.findAll();
            }

            // Search by uniqueId only
            if (uniqueId != null && !uniqueId.isEmpty()
                    && (firstName == null || firstName.isEmpty())
                    && (lastName == null || lastName.isEmpty())) {
                Optional<Paitent> patientOpt = patientRepository.findByUserUniqueIdIgnoreCase(uniqueId);
                return patientOpt.map(List::of).orElse(Collections.emptyList());
            }

            // Search by first and/or last name only
            if ((uniqueId == null || uniqueId.isEmpty())) {
                if (firstName != null && !firstName.isEmpty() && lastName != null && !lastName.isEmpty()) {
                    return patientRepository.findByFirstNameIgnoreCaseContainingAndLastNameIgnoreCaseContaining(firstName, lastName);
                } else if (firstName != null && !firstName.isEmpty()) {
                    return patientRepository.findByFirstNameIgnoreCaseContaining(firstName);
                } else if (lastName != null && !lastName.isEmpty()) {
                    return patientRepository.findByLastNameIgnoreCaseContaining(lastName);
                }
            }

            // Search by combination: uniqueId + firstName + lastName
            if (uniqueId != null && !uniqueId.isEmpty()) {
                if (firstName != null && !firstName.isEmpty() && lastName != null && !lastName.isEmpty()) {
                    return patientRepository.findByFirstNameIgnoreCaseContainingAndLastNameIgnoreCaseContainingAndUserUniqueIdIgnoreCase(firstName, lastName, uniqueId);
                } else if (firstName != null && !firstName.isEmpty()) {
                    return patientRepository.findByFirstNameIgnoreCaseContainingAndUserUniqueIdIgnoreCase(firstName, uniqueId);
                } else if (lastName != null && !lastName.isEmpty()) {
                    return patientRepository.findByLastNameIgnoreCaseContainingAndUserUniqueIdIgnoreCase(lastName, uniqueId);
                } else {
                    // Only uniqueId
                    Optional<Paitent> patientOpt = patientRepository.findByUserUniqueIdIgnoreCase(uniqueId);
                    return patientOpt.map(List::of).orElse(Collections.emptyList());
                }
            }

            return Collections.emptyList();
        }




    public Paitent getPatientByUsername(String uniqueId) {
        return patientRepository.findByUserUniqueId(uniqueId)
                .orElseThrow(() -> new RuntimeException("Patient not found"));
    }

    // Optional: fetch patient by primary key (ID)
    public Paitent getPatientByIdZ(Long patientId) {
        return patientRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found"));
    }

}

