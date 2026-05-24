package com.example.HospitalManagement.controller;

import com.example.HospitalManagement.model.Doctor;
import com.example.HospitalManagement.service.DoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class DoctorController{

    private final DoctorService doctorService;

    // Show Add Doctor Page
    @GetMapping("/add-doctor")
    public String showAddDoctorForm(Model model) {
        model.addAttribute("doctor", new Doctor());
        return "admin/add-doctor";
    }

    // Save Doctor
    @PostMapping("/add-doctor")
    public String addDoctor(@ModelAttribute Doctor doctor) {
        doctorService.saveDoctor(doctor);
        return "redirect:/admin/dashboard";
    }

    // View All Doctors
    @GetMapping("/view-doctors")
    public String viewDoctors(Model model) {
        List<Doctor> doctors = doctorService.getAllDoctors();
        model.addAttribute("doctors", doctors);
        return "admin/view-doctors";
    }


/*
    // Search Doctor
    @GetMapping("/search-doctor")
    public String searchDoctor(@RequestParam String location,
                               @RequestParam String specialty,
                               Model model) {

        List<Doctor> doctors =
                doctorService.findByLocationAndSpecialty(location, specialty);

        model.addAttribute("doctors", doctors);
        return "admin/view-doctors";
    }
    */

    // Show Edit Doctor Form
    @GetMapping("/edit-doctor/{id}")
    public String showEditDoctorForm(@PathVariable Long id, Model model) {
        Doctor doctor = doctorService.getDoctorById(id); // fetch doctor from DB
        model.addAttribute("doctor", doctor);
        return "admin/edit-doctor"; // thymeleaf template
    }

    @PostMapping("/edit-doctor")
    public String updateDoctor(@ModelAttribute Doctor doctor) {
        doctorService.updateDoctor(doctor); // call service to update
        return "redirect:/admin/view-doctors"; // redirect to list
    }


}
