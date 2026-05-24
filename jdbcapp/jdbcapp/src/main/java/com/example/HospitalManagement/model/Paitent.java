package com.example.HospitalManagement.model;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Entity
//@Scope("prototype")
@Data
@Table(name = "patient")
public class Paitent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String firstName;
    private String middleName;
    private String lastName;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dob;
    private String address;
    private String state;
    private String country;

    private String mobileNumber;
    private String relativeName;
    private String relativeMobile;
    private String illnessDetails;

    private String requirement;
   // private String password;
   // private String confirmPassword;

   @OneToOne
   @JoinColumn(name = "user_id")
   private User user;




}
