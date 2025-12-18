package org.ust.project.dto;

<<<<<<< HEAD
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

=======
>>>>>>> 68b99d2cf5e895caf90a504b3cc67ad570396376
import java.time.LocalDate;

<<<<<<< HEAD
@Getter
@Setter
=======
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
>>>>>>> 68b99d2cf5e895caf90a504b3cc67ad570396376
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
