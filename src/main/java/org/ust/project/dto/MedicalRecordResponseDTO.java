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
public class MedicalRecordResponseDTO {

    private Long id;

    private LocalDate recordDate;

    private String diagnosis;

    private String treatmentPlan;

    private String symptoms;

    private DoctorResponseDTO doctor;  // Nested DTO for doctor details

    private PatientResponseDTO patient;  // Nested DTO for patient details
}
