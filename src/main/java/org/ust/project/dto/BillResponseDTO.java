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
public class BillResponseDTO {

    private Long id;

    private LocalDate issueDate;

    private Double totalAmount;

    private String paymentStatus;

    private LocalDate dueDate;

    private AppointmentResponseDTO appointment; // Nested DTO for the appointment linked with the bill

    public BillResponseDTO(Long id, LocalDate issueDate, Double totalAmount, String paymentStatus, LocalDate dueDate) {
        this.id = id;
        this.issueDate = issueDate;
        this.totalAmount = totalAmount;
        this.paymentStatus = paymentStatus;
        this.dueDate = dueDate;
    }


    
}
