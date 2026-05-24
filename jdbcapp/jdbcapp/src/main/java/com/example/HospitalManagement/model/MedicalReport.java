package com.example.HospitalManagement.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class MedicalReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fileName;

    private String fileType;

    private String filePath;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Paitent patient;

    // Add this field
    private LocalDateTime uploadDate;
}