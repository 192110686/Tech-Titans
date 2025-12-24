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
public class ConsultationResponseDTO {

    private Long id;
    private LocalDate consultationDate;
    private String reasonForVisit;
    private String notes;

    private AppointmentResponseDTO appointment;
    private BillResponseDTO bill;  // Ensure bill is present
    private PrescriptionResponseDTO prescription;  // Ensure prescription is present

    private String consultationStatus; // COMPLETED / CANCELLED
}
