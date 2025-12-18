package org.ust.project.dto;

import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
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
