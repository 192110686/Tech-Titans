package org.ust.project.dto;

import java.time.LocalDate;
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
    private String timeSlot;
    private String status;
    private String reasonForVisit;
    private DoctorResponseDTO doctor;
    private PatientResponseDTO patient;
      // Constructor that matches the parameters being passed
    public AppointmentResponseDTO(Long id, LocalDateTime appointmentDateTime, String reasonForVisit, 
                                  String status, DoctorResponseDTO doctor, PatientResponseDTO patient) {
        this.id = id;
        this.appointmentDateTime = appointmentDateTime;
        this.reasonForVisit = reasonForVisit;
        this.status = status;
        this.doctor = doctor;
        this.patient = patient;
    }
}
