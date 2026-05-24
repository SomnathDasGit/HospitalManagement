package com.example.HospitalManagement.model;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "report_request")
@Getter
@Setter
public class ReportRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String status;

    private LocalDateTime requestedAt;

    // getters setters
}
