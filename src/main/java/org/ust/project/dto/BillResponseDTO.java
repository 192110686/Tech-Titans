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
public class BillResponseDTO {

    private Long id;

    private LocalDate issueDate;

    private Double totalAmount;

    private String paymentStatus;

    private LocalDate dueDate;

    private AppointmentResponseDTO appointment;  // Nested DTO for the appointment linked with the bill
}
