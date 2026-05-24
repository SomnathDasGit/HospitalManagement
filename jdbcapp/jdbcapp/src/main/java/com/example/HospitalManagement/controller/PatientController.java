package com.example.HospitalManagement.controller;

import com.example.HospitalManagement.model.*;
import com.example.HospitalManagement.service.*;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/patient")

@RequiredArgsConstructor
public class PatientController {


    //private UserService userService;
    private final UserService userService;
    private final PatientService patientService;
    private final DoctorService doctorService;
    private final AppointmentService appointmentService;
    private final MedicalReportService reportService;


    /*
    @GetMapping("/my-report/{id}")
    public ResponseEntity<Resource> downloadReport(@PathVariable long id,
                                                   Principal principal) throws IOException {
        Paitent patient = patientService.getPatientById(id);

        // Optional: check if logged-in patient matches requested id
        // String loggedInUsername = principal.getName();

        if (patient.getReportFileName() == null) {
            return ResponseEntity.notFound().build();
        }

        Path path = Paths.get("src/main/uploads/").resolve(patient.getReportFileName());
        Resource resource = new UrlResource(path.toUri());

        if (!resource.exists()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    */

    @GetMapping("/view-doctor")
    public String viewDoctor(Model model) {
        List<Doctor> doctors = doctorService.getAllDoctors();
        model.addAttribute("doctors", doctors);
        return "/patient/view-doctor";
    }



    @GetMapping("/edit-details")
    public String showEditPatient(Model model) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        User user = userService.findByUniqueId(username);
        Paitent patient = patientService.findByUser(user);

        model.addAttribute("patient", patient);

        return "/patient/edit-details";
    }


    @PostMapping("/update-patient")
    public String updatePatient(
            @ModelAttribute("patient") Paitent formPatient,
            RedirectAttributes redirectAttributes) {

        Paitent existingPatient = patientService.getPatientById(formPatient.getId());

        if (existingPatient == null) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Patient not found.");
            return "redirect:/patient/profile";
        }

        existingPatient.setFirstName(formPatient.getFirstName());
        existingPatient.setMiddleName(formPatient.getMiddleName());
        existingPatient.setLastName(formPatient.getLastName());
        existingPatient.setDob(formPatient.getDob());
        existingPatient.setAddress(formPatient.getAddress());
        existingPatient.setState(formPatient.getState());
        existingPatient.setCountry(formPatient.getCountry());
        existingPatient.setMobileNumber(formPatient.getMobileNumber());
        existingPatient.setRelativeName(formPatient.getRelativeName());
        existingPatient.setRelativeMobile(formPatient.getRelativeMobile());
        existingPatient.setIllnessDetails(formPatient.getIllnessDetails());
        existingPatient.setRequirement(formPatient.getRequirement());

        patientService.updatePatient(existingPatient);

        redirectAttributes.addFlashAttribute("successMessage",
                "Patient details updated successfully!");

        return "redirect:/patient/profile";
    }

    @GetMapping("/profile")
    public String viewProfile(Model model) {

        // 1 Get authentication object
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        // 2 Get username (Spring stores it internally)
        String username = authentication.getName();

        // 3 Fetch User from DB
        User user = userService.findByUniqueId(username);


        // 4 Fetch Patient linked to that user
        Paitent patient = patientService.findByUser(user);

        if (patient == null) {
            throw new RuntimeException("Patient not found");
        }

        model.addAttribute("patient", patient);

        return "/patient/profile";
    }

    @GetMapping("/find-doctor")
    public String findDoctor(
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String specialty,
            Model model) {

        List<Doctor> doctors =
                doctorService.searchDoctors(location, specialty);

        model.addAttribute("doctors", doctors);

        return "/patient/find-doctor";
    }

    @GetMapping("/appointments")
    public String viewAppointments(
            @AuthenticationPrincipal UserDetails userDetails,
            Model model) {

        User user = userService
                .findByUniqueId(userDetails.getUsername());

        Paitent patient = patientService.findByUser(user);

        List<Appointment> appointments =
                appointmentService.getUpcomingAppointments(patient);

        model.addAttribute("appointments", appointments);

        return "patient/appointments";
    }

    @GetMapping("/view-All-doctor")
    public String viewDoctors(Model model) {
        List<Doctor> doctors = doctorService.getAllDoctors();
        model.addAttribute("doctors", doctors);
        return "patient/view-All-doctor";
    }



    @GetMapping("/reports")
    public String viewReports(Model model, Principal principal) {

        // Fetch the patient by uniqueId (username)
        Paitent patient = patientService.getPatientByUsername(principal.getName());
        Long patientId = patient.getId();

        // Fetch reports for this patient
        List<MedicalReport> reports = reportService.getReportsByPatientId(patientId);

        model.addAttribute("reports", reports);
        model.addAttribute("firstName", patient.getFirstName());
        model.addAttribute("lastName", patient.getLastName());
        model.addAttribute("patientId", patientId);

        return "/patient/reports";
    }
    // Download a specific report
    @GetMapping("/download/{reportId}")
    public ResponseEntity<InputStreamResource> downloadReport(@PathVariable Long reportId, Principal principal) throws Exception {

        // Fetch the patient by uniqueId (username)
        Paitent patient = patientService.getPatientByUsername(principal.getName());
        Long patientId = patient.getId();

        // Get the file, ensure it belongs to this patient
        File file = reportService.getReportFile(reportId, patientId);

        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + file.getName())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .contentLength(file.length())
                .body(resource);
    }


}

