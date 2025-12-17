package org.ust.project.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class AppointmentResponseDTO {

    private Long id;

    private LocalDate appointmentDate;

    private String timeSlot;

    private String status;

    private String reasonForVisit;

    private DoctorResponseDTO doctor;  // Nested DTO for doctor details

    private PatientResponseDTO patient;  // Nested DTO for patient details
}
