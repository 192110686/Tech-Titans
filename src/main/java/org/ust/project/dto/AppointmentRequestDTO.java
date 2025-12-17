package org.ust.project.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class AppointmentRequestDTO {

    private LocalDate appointmentDate;

    private String timeSlot;

    private String reasonForVisit;

    private Long doctorId;

    private Long patientId;
}
