package com.example.HospitalManagement.service;

import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private PatientService patientService;

    public void sendExcelReport(String toEmail)
            throws Exception {

        ByteArrayInputStream excelFile =
                patientService.downloadPatientsExcel();

        MimeMessage message =
                mailSender.createMimeMessage();

        MimeMessageHelper helper =
                new MimeMessageHelper(message, true);

        helper.setTo(toEmail);

        helper.setSubject("Patient Report");

        helper.setText(
                "Please find attached patient report."
        );

        helper.addAttachment(
                "patients.xlsx",
                new InputStreamResource(excelFile)
        );

        mailSender.send(message);
    }
}