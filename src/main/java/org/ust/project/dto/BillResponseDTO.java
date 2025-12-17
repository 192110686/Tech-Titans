package org.ust.project.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class BillResponseDTO {

    private Long id;

    private LocalDate issueDate;

    private Double totalAmount;

    private String paymentStatus;

    private LocalDate dueDate;

    private AppointmentResponseDTO appointment;  // Nested DTO for the appointment linked with the bill
}
