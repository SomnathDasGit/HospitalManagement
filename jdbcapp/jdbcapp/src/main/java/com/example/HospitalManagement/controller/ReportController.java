package com.example.HospitalManagement.controller;

import com.example.HospitalManagement.model.ReportRequest;
import com.example.HospitalManagement.model.User;
import com.example.HospitalManagement.repo.ReportRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController

public class ReportController {

    @Autowired
    private ReportRequestRepository repository;

    @GetMapping("/download")
    public String openPage() {

        return "download"; // Thymeleaf HTML
    }

    @PostMapping("/download")
    public ResponseEntity<String> generateReport(
            @RequestBody ReportRequest dto
    ) {

        ReportRequest request =
                new ReportRequest();

        request.setEmail(dto.getEmail());

        request.setStatus("PENDING");

        request.setRequestedAt(LocalDateTime.now());

        repository.save(request);

        return ResponseEntity.ok(
                "Report generation request submitted"
        );
    }
}
