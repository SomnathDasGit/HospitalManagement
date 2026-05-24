package com.example.HospitalManagement.repo;


import com.example.HospitalManagement.model.Appointment;
import com.example.HospitalManagement.model.Paitent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    // Get booked slots for a doctor on a particular date
    @Query("SELECT a.timeSlot FROM Appointment a " +
            "WHERE a.doctor.id = :doctorId " +
            "AND a.appointmentDate = :date " +
            "AND a.status = 'BOOKED'")
    List<String> findBookedSlots(@Param("doctorId") Long doctorId,
                                 @Param("date") LocalDate date);


    // Get upcoming appointments of a patient
    List<Appointment> findByPatientAndAppointmentDateGreaterThanEqualOrderByAppointmentDateAsc(
            Paitent patient,
            LocalDate date
    );

}
