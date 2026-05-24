package com.example.HospitalManagement.controller;

import com.example.HospitalManagement.model.*;
import com.example.HospitalManagement.service.*;
//change 33333
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final PatientService patientService;

    private final DoctorService doctorService;
    private final UserService userService;
    private final AppointmentService appointmentService;
    private final MedicalReportService reportService;

    // ---------------- DASHBOARD ----------------
    //@GetMapping("/dashboard")
   // public String adminAashboard() {
       // return "admin/dashboard";
   // }

    // ---------------- PATIENT ----------------

//
//    @GetMapping("/patients/download")
//    public ResponseEntity<InputStreamResource> downloadPatients() {
//
//        InputStreamResource file = new InputStreamResource(
//                patientService.downloadPatientsExcel()
//        );
//
//        return ResponseEntity.ok()
//                .header(
//                        HttpHeaders.CONTENT_DISPOSITION,
//                        "attachment; filename=patients.xlsx"
//                )
//                .contentType(
//                        MediaType.parseMediaType(
//                                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
//                        )
//                )
//                .body(file);
//    }



    @GetMapping("/add-patient")
    public String showAddPatientForm(Model model) {
        model.addAttribute("patient", new Paitent());
        return "admin/add-patient";
    }

    @PostMapping("/add-patient")
    public String addPatient(@Valid @ModelAttribute("patient") Paitent paitent,
                             BindingResult result,
                             @RequestParam String password,
                             @RequestParam String confirmPassword,
                             Model model) {
        if (result.hasErrors()) {
            //System.out.println(" post err reg called");
            return "admin/add-patient";

        }



        //Model is used to pass data from controller → view (HTML page).
        if (!password.equals(confirmPassword)) {
            model.addAttribute("error", "Passwords do not match");

            //System.out.println(" post passworderror reg called");
            return "admin/add-patient";
        }

        // Create user
        User user = userService.createUser(password, Role.PATIENT);

        // Link patient to use;
        paitent.setUser(user);
        //paitentService.savePaitent(paitent);
        patientService.addPatient(paitent);


        model.addAttribute("message",
                "Registration successful. New Patient's ID is: " + user.getUniqueId());
        //System.out.println(" post reg called" + user.getUniqueId());
        //return "register";

        return "/admin/add-patient";
    }


    @GetMapping("/add-admin")
    public String addAdminPage(Model model) {
        model.addAttribute("user", new User()); // <- This is required!
        return "/admin/add-admin";
    }
    @PostMapping("/add-admin")
    public String saveAdmin(@Valid @ModelAttribute("user") User user,
                            @RequestParam String password,
                            Model model) {

        // Create the admin and get the saved object
        User admin = userService.createAdmin(user, password, Role.ADMIN); // make it return User

        // Add a success message to the model
        model.addAttribute("message",
                "Registration successful. Your ID is: " + admin.getUniqueId());

        // Pass an empty form object for the next admin registration
        model.addAttribute("user", new User());

        return "admin/add-admin";
    }


/*
    @GetMapping("/search-patient")
    public String searchPatient(
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName,
            Model model) {

        List<Paitent> patients = new ArrayList<>();
        boolean searched = false; // flag to indicate user searched

        if ((firstName != null && !firstName.isEmpty()) ||
                (lastName != null && !lastName.isEmpty())) {

            patients = patientService.searchPatients(firstName, lastName);
            searched = true;
        }

        model.addAttribute("patients", patients);
        model.addAttribute("searched", searched);  // pass flag to template

        return "admin/search-patient";
    }
*/
@GetMapping("/search-patient")
public String searchPatient(
        @RequestParam(required = false) String firstName,
        @RequestParam(required = false) String lastName,
        @RequestParam(required = false) String uniqueId,
        Model model) {

    List<Paitent> patients = patientService.searchPatients(firstName, lastName, uniqueId);


    //error
    boolean hasInput =
            (firstName != null && !firstName.trim().isEmpty()) ||
                    (lastName != null && !lastName.trim().isEmpty()) ||
                    (uniqueId != null && !uniqueId.trim().isEmpty());

    // show error only when input exists but no result
    if (hasInput && patients.isEmpty()) {
        model.addAttribute("errorMessage", "No patient found matching the provided details");
    }



    model.addAttribute("patients", patients);
    model.addAttribute("firstName", firstName);
    model.addAttribute("lastName", lastName);
    model.addAttribute("uniqueId", uniqueId);

    return "admin/search-patient";
}









    @GetMapping("/edit-patient/{id}")
    public String showEditPatient(@PathVariable long id, Model model) {
        Paitent patient = patientService.getPatientById(id);
        model.addAttribute("patient", patient);
        return "admin/edit-patient";
    }

    @PostMapping("/update-patient")
    public String updatePatient(
            @ModelAttribute("patient") Paitent formPatient,
            RedirectAttributes redirectAttributes) {

        try {
            // Fetch existing patient from DB
            Paitent existingPatient = patientService.getPatientById(formPatient.getId());

            if (existingPatient == null) {
                redirectAttributes.addFlashAttribute("errorMessage", "Patient not found.");
                return "redirect:/admin/search-patient";
            }

            // Update only editable fields
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

            // Save updated patient
            patientService.updatePatient(existingPatient);

            redirectAttributes.addFlashAttribute("successMessage", "Patient details updated successfully!");

        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", "Something went wrong while updating patient.");
            return "redirect:/admin/edit-patient/" + formPatient.getId();
        }

        return "redirect:/admin/search-patient";
    }


    @GetMapping("/find-doctor")
    public String findDoctor(
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String specialty,
            Model model) {

        List<Doctor> doctors =
                doctorService.searchDoctors(location, specialty);

        model.addAttribute("doctors", doctors);

        return "/admin/find-doctor";
    }

    @GetMapping("/book-appointment/{doctorId}")
    public String showBookingPage(@PathVariable Long doctorId, Model model) {

        Doctor doctor = doctorService.getDoctorById(doctorId);

        model.addAttribute("doctor", doctor);
        model.addAttribute("appointment", new Appointment());

        return "admin/book-appointment";
    }

    @PostMapping("/save-appointment")
    public String saveAppointment(
            @ModelAttribute Appointment appointment,
            @RequestParam Long doctorId,
            @RequestParam Long patientId,
            RedirectAttributes redirectAttributes) {

        Doctor doctor = doctorService.getDoctorById(doctorId);
        Paitent patient = patientService.getPatientById(patientId);

        appointment.setDoctor(doctor);
        appointment.setPatient(patient);
        appointment.setStatus("BOOKED");

        appointmentService.save(appointment);

        redirectAttributes.addFlashAttribute("successMessage",
                "Appointment booked successfully!");

        return "redirect:/admin/find-doctor";
    }

    @GetMapping("/upload-report")
    public String addReportPage(Model model) {
        model.addAttribute("report", new MedicalReport());
        return "admin/upload-report";
    }

    @PostMapping("/upload-report")
    public String uploadReport(
            @RequestParam Long patientId,
            @RequestParam("file") MultipartFile file,
            Model model) {

        try {
            reportService.uploadReport(patientId, file);
            model.addAttribute("successMessage", "Report uploaded successfully");
        } catch (Exception e) {
            e.printStackTrace(); // <--- log exception
            model.addAttribute("errorMessage", "Upload failed: " + e.getMessage());
        }

        return "admin/upload-report";
    }


}
