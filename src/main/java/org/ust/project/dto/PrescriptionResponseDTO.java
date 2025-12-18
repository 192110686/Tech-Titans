package org.ust.project.dto;
import lombok.Getter;
import lombok.Setter;


import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor

public class PrescriptionResponseDTO {

    private Long id;

    private String medicationName;

    private Double dosageMg;

    private Double price;

    private Double frequency;

    private LocalDate startDate;

    private LocalDate endDate;

    private AppointmentResponseDTO appointment;  // Nested DTO for appointment details

    private MedicalRecordResponseDTO medicalRecord;  // Nested DTO for medical record details

}
