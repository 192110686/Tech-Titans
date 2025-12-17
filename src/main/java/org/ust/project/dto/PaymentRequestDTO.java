package org.ust.project.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class PaymentRequestDTO {

    private LocalDate paymentDate;

    private Double amountPaid;

    private String paymentMethod;

    private Long billId;  // The associated bill ID
}
