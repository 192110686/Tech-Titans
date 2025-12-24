package org.ust.project.repo;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.ust.project.model.Appointment;
import org.ust.project.model.Doctor;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findByDoctorId(Long doctorId);
    List<Appointment> findByPatientId(Long patientId);
    boolean existsByDoctorIdAndAppointmentDateAndTimeSlot(
    	    Long doctorId,
    	    LocalDateTime appointmentDateTime
    	);
   // Query to check if doctor is available at a specific time
    List<Appointment> findByDoctorAndAppointmentDateTime(Doctor doctor, LocalDateTime appointmentDateTime);

    // Query to find appointments in a time range
    List<Appointment> findByDoctorAndAppointmentDateTimeBetween(Doctor doctor, LocalDateTime startTime, LocalDateTime endTime);
    // Custom query to check if the doctor has an appointment at the requested time
    boolean existsByDoctorIdAndAppointmentDateTime(Long doctorId, LocalDateTime appointmentDateTime);

}