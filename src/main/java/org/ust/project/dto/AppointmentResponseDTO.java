package org.ust.project.dto;

import java.time.LocalDateTime;

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
public class AppointmentResponseDTO {

    private Long id;
    private LocalDateTime appointmentDateTime;
    private String status;
    private String reasonForVisit;
    private DoctorResponseDTO doctor;
    private PatientResponseDTO patient;
      // Constructor that matches the parameters being passed
   
}
