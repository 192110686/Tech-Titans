package org.ust.project.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class BillRequestDTO {

    private LocalDate issueDate;

    private Double totalAmount;

    private String paymentStatus;

    private LocalDate dueDate;

    private Long appointmentId;  // The associated appointment ID
}
