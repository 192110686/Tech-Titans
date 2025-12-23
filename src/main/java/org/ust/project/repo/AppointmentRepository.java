package org.ust.project.repo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.ust.project.model.Appointment;

import java.util.List;
import java.time.LocalDate;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findByDoctorId(Long doctorId);
    List<Appointment> findByPatientId(Long patientId);
    List<Appointment> findByAppointmentDate(LocalDate date);
    boolean existsByDoctorIdAndAppointmentDateAndTimeSlot(
    	    Long doctorId,
    	    LocalDate appointmentDate,
    	    String timeSlot
    	);
}