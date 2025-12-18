package org.ust.project.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class MedicalRecordRequestDTO {

    private LocalDate recordDate;

    private String diagnosis;

    private String treatmentPlan;

    private String symptoms;

    private Long patientId;  // The associated patient ID

    private Long doctorId;   // The associated doctor ID
}
