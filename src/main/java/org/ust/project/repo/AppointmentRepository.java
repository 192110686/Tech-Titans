package org.ust.project.repo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.ust.project.model.Appointment;
import org.ust.project.model.Doctor;

import java.util.List;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findByDoctorId(Long doctorId);
    List<Appointment> findByPatientId(Long patientId);
    List<Appointment> findByAppointmentDate(LocalDate date);
    boolean existsByDoctorIdAndAppointmentDateAndTimeSlot(
    	    Long doctorId,
    	    LocalDateTime appointmentDate,
    	    String timeSlot
    	);
   // Query to check if doctor is available at a specific time
    List<Appointment> findByDoctorAndAppointmentDateTime(Doctor doctor, LocalDateTime appointmentDateTime);

    // Query to find appointments in a time range
    List<Appointment> findByDoctorAndAppointmentDateTimeBetween(Doctor doctor, LocalDateTime startTime, LocalDateTime endTime);
    // Custom query to check if the doctor has an appointment at the requested time
    boolean existsByDoctorIdAndAppointmentDateTime(Long doctorId, LocalDateTime appointmentDateTime);

}