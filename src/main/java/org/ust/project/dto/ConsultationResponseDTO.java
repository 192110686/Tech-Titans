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

    private Long id;  // ID of the consultation
    private LocalDate consultationDate;   // Date of the consultation
    private String reasonForVisit;         // Reason for the visit (e.g., checkup, fever, etc.)
    private String notes;                  // Additional notes from the consultation

    // Nested DTO for the associated Appointment
    private AppointmentResponseDTO appointment;

    // Nested DTO for the associated Bill
    private BillResponseDTO bill;

    // Nested DTO for the associated Prescription
    private PrescriptionResponseDTO prescription;
}
