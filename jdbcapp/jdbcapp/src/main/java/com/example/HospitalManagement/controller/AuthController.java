package com.example.HospitalManagement.controller;

import com.example.HospitalManagement.model.Paitent;
import com.example.HospitalManagement.model.Role;
import com.example.HospitalManagement.model.User;
//import com.example.HospitalManagement.service.PaitentService;
import com.example.HospitalManagement.service.CustomUserDetailsService;
import com.example.HospitalManagement.service.PatientService;
import com.example.HospitalManagement.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller

@RequiredArgsConstructor
public class AuthController {



    private  final UserService userService;
    private final PatientService patientService;

    @GetMapping("/")
    public String home()
    {
        return "index";
    }


    @GetMapping("/login")
    public String login(HttpSession session)
    {

        //session.setAttribute("uniqueId",  uniqueId);
        return "login";
    }

    @GetMapping("/register")

    //public String registerForm(Model model)
    public String registerForm(Model model) {
        model.addAttribute("patient", new Paitent());
        //System.out.println(" get reg called");
        return "register";
    }

    @PostMapping("/register")
    public String registerPatient(@Valid @ModelAttribute("patient") Paitent paitent,
                                  BindingResult result,
                                  @RequestParam String password,
                                  @RequestParam String confirmPassword,
                                  Model model) {

        if (result.hasErrors()) {
            //System.out.println(" post err reg called");
            return "register";

        }

        //Model is used to pass data from controller → view (HTML page).
        if (!password.equals(confirmPassword)) {
            model.addAttribute("error", "Passwords do not match");

            //System.out.println(" post passworderror reg called");
            return "register";
        }

        // Create user
        User user = userService.createUser(password, Role.PATIENT);

        // Link patient to use;
        paitent.setUser(user);
        //paitentService.savePaitent(paitent);
        patientService.addPatient(paitent);


        model.addAttribute("message",
                "Registration successful. Your ID is: " + user.getUniqueId());
        //System.out.println(" post reg called" + user.getUniqueId());
        return "register";
    }

    //@AuthenticationPrincipal UserDetails userDetails  --> Give me the authenticated user object.
    //Model is used to pass data from controller → view (HTML page).
    // General user dashboard
    @GetMapping("/patient/dashboard")
    public String userDashboard(@AuthenticationPrincipal  UserDetails userDetailsU) {





        return "patient/dashboard"; // src/main/resources/templates/patient/dashboard.html
    }

    // Admin dashboard
    @GetMapping("/admin/dashboard")
    public String adminDashboard(@AuthenticationPrincipal UserDetails userDetails) {
        return "admin/dashboard"; // src/main/resources/templates/admin/dashboard.html
    }

    @GetMapping("/logout")
    public String logout(HttpSession session)
    {

        //session.setAttribute("uniqueId",  uniqueId);
        return "index";
    }
}