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

    // Always present
    private AppointmentResponseDTO appointment;

    // Optional – may be null initially
    private BillResponseDTO bill;

    // Optional – may be null initially
    private PrescriptionResponseDTO prescription;
    
    private String consultationStatus; // COMPLETED / CANCELLED

}
