package org.ust.project.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonInclude;

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
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ConsultationResponseDTO {

    private Long id;
    private LocalDate consultationDate;
    private String reasonForVisit;
    private String notes;

    private AppointmentResponseDTO appointment;
    private BillResponseDTO bill;  // Ensure bill is present
    private PrescriptionResponseDTO prescription;  // Ensure prescription is present

}
