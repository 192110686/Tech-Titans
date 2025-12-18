package org.ust.project.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequestDTO {

    private LocalDate paymentDate;

    private Double amountPaid;

    private String paymentMethod;

    private Long billId;  // The associated bill ID
}
