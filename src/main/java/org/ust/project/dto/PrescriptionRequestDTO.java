package org.ust.project.dto;

import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
public class PrescriptionRequestDTO {

    private String medicationName;

    private Double dosageMg;

    private Double price;

    private Double frequency;

    private LocalDate startDate;

    private LocalDate endDate;

    private Long appointmentId;  // Linking to the Appointment

    private Long medicalRecordId;  // Linking to the MedicalRecord

}
