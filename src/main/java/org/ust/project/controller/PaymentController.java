package org.ust.project.controller;

import java.util.List;

import org.ust.project.dto.PaymentRequestDTO;
import org.ust.project.dto.PaymentResponseDTO;
import org.ust.project.service.PaymentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    /* ================= CREATE PAYMENT ================= */
    @PostMapping
    public ResponseEntity<PaymentResponseDTO> createPayment(
            @RequestBody PaymentRequestDTO requestDTO) {

        PaymentResponseDTO response =
                paymentService.createPayment(requestDTO);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /* ================= GET ALL PAYMENTS ================= */
    @GetMapping
    public ResponseEntity<List<PaymentResponseDTO>> getAllPayments() {

        return ResponseEntity.ok(paymentService.getAllPayments());
    }

    /* ================= GET PAYMENT BY ID ================= */
    @GetMapping("/{id}")
    public ResponseEntity<PaymentResponseDTO> getPaymentById(
            @PathVariable Long id) {

        return ResponseEntity.ok(paymentService.getPaymentById(id));
    }

    /* ================= UPDATE PAYMENT ================= */
    @PutMapping("/{id}")
    public ResponseEntity<PaymentResponseDTO> updatePayment(
            @PathVariable Long id,
            @RequestBody PaymentRequestDTO requestDTO) {

        return ResponseEntity.ok(
                paymentService.updatePayment(id, requestDTO)
        );
    }

    /* ================= DELETE PAYMENT ================= */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePayment(@PathVariable Long id) {

        paymentService.deletePayment(id);
        return ResponseEntity.noContent().build();
    }
}
