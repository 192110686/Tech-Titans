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
