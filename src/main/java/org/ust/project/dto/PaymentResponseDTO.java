package org.ust.project.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonInclude;

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
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PaymentResponseDTO {

    private Long id;

    private LocalDate paymentDate;

    private Double amountPaid;

    private String paymentMethod;

    private BillResponseDTO bill;  // Nested DTO for the bill linked with the payment
}
