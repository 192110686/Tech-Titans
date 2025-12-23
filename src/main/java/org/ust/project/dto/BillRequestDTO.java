package org.ust.project.dto;

import java.time.LocalDate;

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
public class BillRequestDTO {

    private LocalDate issueDate;

    private Double totalAmount;

    private String paymentStatus;

    private LocalDate dueDate;

    // private Long appointmentId;// The associated appointment ID
    
    // private Long patientId;// The associated patient ID
    
    private Long paymentId;
}
