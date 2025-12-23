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

    private ConsultationResponseDTO consultation;
}
