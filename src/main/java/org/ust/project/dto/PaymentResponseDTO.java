package org.ust.project.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class PaymentResponseDTO {

    private Long id;

    private LocalDate paymentDate;

    private Double amountPaid;

    private String paymentMethod;

    private BillResponseDTO bill;  // Nested DTO for the bill linked with the payment
}
