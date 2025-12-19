package org.ust.project.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AppointmentResponseDTO {

    private Long id;

    private LocalDate appointmentDate;

    private String timeSlot;

    private String status;

    private String reasonForVisit;

    private DoctorResponseDTO doctor;  // Nested DTO for doctor details

    private PatientResponseDTO patient;  // Nested DTO for patient details
    
     public AppointmentResponseDTO(Long id, LocalDate appointmentDate, DoctorResponseDTO doctor, PatientResponseDTO patient) {
        this.id = id;
        this.appointmentDate = appointmentDate;
        this.doctor = doctor;
        this.patient = patient;
    }
}
