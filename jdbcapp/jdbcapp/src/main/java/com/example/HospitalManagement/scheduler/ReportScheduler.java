package com.example.HospitalManagement.scheduler;

import com.example.HospitalManagement.model.ReportRequest;
import com.example.HospitalManagement.repo.ReportRequestRepository;
import com.example.HospitalManagement.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ReportScheduler {

    @Autowired
    private ReportRequestRepository repository;

    @Autowired
    private EmailService emailService;

    @Scheduled(fixedDelay = 60000)
    public void processReports() {

        List<ReportRequest> requests =
                repository.findByStatus("PENDING");

        for (ReportRequest request : requests) {

            try {

                request.setStatus("PROCESSING");

                repository.save(request);

                emailService.sendExcelReport(
                        request.getEmail()
                );

                request.setStatus("COMPLETED");

            } catch (Exception e) {

                request.setStatus("FAILED");
            }

            repository.save(request);
        }
    }
}
