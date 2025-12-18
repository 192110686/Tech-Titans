package org.ust.project.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

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
}
