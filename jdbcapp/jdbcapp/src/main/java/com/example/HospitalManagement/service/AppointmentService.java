package com.example.HospitalManagement.service;


import com.example.HospitalManagement.model.Appointment;
import com.example.HospitalManagement.model.Paitent;
import com.example.HospitalManagement.repo.AppointmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;

    // Save appointment
    public Appointment save(Appointment appointment) {
        return appointmentRepository.save(appointment);
    }

    // Get appointment by ID
    public Appointment getAppointmentById(Long id) {
        Optional<Appointment> optional = appointmentRepository.findById(id);
        return optional.orElseThrow(() ->
                new RuntimeException("Appointment not found"));
    }

    // ✅ Get available slots
    public List<String> getAvailableSlots(Long doctorId, LocalDate date) {

        // All possible slots (you can modify as needed)
        List<String> allSlots = Arrays.asList(
                "10:00 AM - 10:30 AM",
                "10:30 AM - 11:00 AM",
                "11:00 AM - 11:30 AM",
                "11:30 AM - 12:00 PM",
                "02:00 PM - 02:30 PM",
                "02:30 PM - 03:00 PM"
        );

        // Already booked slots
        List<String> bookedSlots =
                appointmentRepository.findBookedSlots(doctorId, date);

        // Remove booked slots
        return allSlots.stream()
                .filter(slot -> !bookedSlots.contains(slot))
                .collect(Collectors.toList());
    }

    // ✅ Get upcoming appointments for patient
    public List<Appointment> getUpcomingAppointments(Paitent patient) {
        return appointmentRepository
                .findByPatientAndAppointmentDateGreaterThanEqualOrderByAppointmentDateAsc(
                        patient,
                        LocalDate.now()
                );
    }

    // ✅ Cancel appointment
    public void cancelAppointment(Long appointmentId) {
        Appointment appointment = getAppointmentById(appointmentId);
        appointment.setStatus("CANCELLED");
        appointmentRepository.save(appointment);
    }

    /*
    public List<Appointment> getUpcomingAppointments(Long id) {
    }
    /
     */
}
