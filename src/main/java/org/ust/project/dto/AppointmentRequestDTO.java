package org.ust.project.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AppointmentRequestDTO {

    private LocalDate appointmentDate;

    private String timeSlot;

    private String reasonForVisit;

    private Long doctorId;

    private Long patientId;
}
