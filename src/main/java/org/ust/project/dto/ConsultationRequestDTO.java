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
public class ConsultationRequestDTO {

    private Long appointmentId;          // Appointment ID (mandatory)
    private LocalDate consultationDate;  // Date of consultation
    private String reasonForVisit;       // Reason for the visit
    private String notes;                // Notes from the doctor
}
