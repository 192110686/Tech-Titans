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
public class BillRequestDTO {

    private LocalDate issueDate;

    private Double totalAmount;

    private String paymentStatus;

    private LocalDate dueDate;

    private Long appointmentId;// The associated appointment ID
    
    private Long patientId;// The associated patient ID
}
