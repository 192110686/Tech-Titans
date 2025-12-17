package org.ust.project.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class MedicalRecordRequestDTO {

    private LocalDate recordDate;

    private String diagnosis;

    private String treatmentPlan;

    private String symptoms;

    private Long patientId;  // The associated patient ID

    private Long doctorId;   // The associated doctor ID
}
