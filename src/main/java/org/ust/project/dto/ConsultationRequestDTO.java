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

    private Long appointmentId;  // ID of the appointment for which the consultation is happening
    private LocalDate consultationDate;   // Date of the consultation
    private String reasonForVisit;         // Reason for the visit (e.g., checkup, fever, etc.)
    private String notes;               // Additional notes from the consultation
    // private Long prescriptionId;           // ID of the prescription linked to the consultation (optional)
}
