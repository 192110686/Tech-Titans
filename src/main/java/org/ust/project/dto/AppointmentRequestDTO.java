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
public class AppointmentRequestDTO {
    private LocalDateTime appointmentDateTime;
    private String timeSlot;
    private String reasonForVisit;
    private String status;
    private Long doctorId;
    private Long patientId;
    //private Long billId; // Optional, in case there's a bill related to the appointment
}
