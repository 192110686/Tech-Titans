package org.ust.project.service;

import java.util.List;
import java.util.stream.Collectors;

import org.ust.project.dto.BillResponseDTO;
import org.ust.project.dto.PaymentRequestDTO;
import org.ust.project.dto.PaymentResponseDTO;
import org.ust.project.model.Bill;
import org.ust.project.model.Payment;
import org.ust.project.repo.BillRepository;
import org.ust.project.repo.PaymentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;

@Service
@Transactional
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final BillRepository billRepository;

    public PaymentService(PaymentRepository paymentRepository,
                          BillRepository billRepository) {
        this.paymentRepository = paymentRepository;
        this.billRepository = billRepository;
    }

    /* ================= CREATE ================= */
    public PaymentResponseDTO createPayment(PaymentRequestDTO dto) {

        Bill bill = billRepository.findById(dto.getBillId())
                .orElseThrow(() ->
                        new EntityNotFoundException("Bill not found with id: " + dto.getBillId()));

        Payment payment = new Payment();
        payment.setPaymentDate(dto.getPaymentDate());
        payment.setAmountPaid(dto.getAmountPaid());
        payment.setPaymentMethod(dto.getPaymentMethod());
        payment.setBill(bill);

        Payment savedPayment = paymentRepository.save(payment);

        // Optional domain logic
        // bill.setPaymentStatus("PAID");

        return toResponseDTO(savedPayment);
    }

    /* ================= GET BY ID ================= */
    public PaymentResponseDTO getPaymentById(Long id) {

        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("Payment not found with id: " + id));

        return toResponseDTO(payment);
    }

    /* ================= GET ALL ================= */
    public List<PaymentResponseDTO> getAllPayments() {

        return paymentRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    /* ================= UPDATE ================= */
    public PaymentResponseDTO updatePayment(Long id, PaymentRequestDTO dto) {

        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("Payment not found with id: " + id));

        Bill bill = billRepository.findById(dto.getBillId())
                .orElseThrow(() ->
                        new EntityNotFoundException("Bill not found with id: " + dto.getBillId()));

        payment.setPaymentDate(dto.getPaymentDate());
        payment.setAmountPaid(dto.getAmountPaid());
        payment.setPaymentMethod(dto.getPaymentMethod());
        payment.setBill(bill);

        Payment updatedPayment = paymentRepository.save(payment);

        return toResponseDTO(updatedPayment);
    }

    /* ================= DELETE ================= */
    public void deletePayment(Long id) {

        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("Payment not found with id: " + id));

        paymentRepository.delete(payment);
    }

    /* ================= DTO MAPPER ================= */
    private PaymentResponseDTO toResponseDTO(Payment payment) {

        Bill bill = payment.getBill();

        BillResponseDTO billDTO = new BillResponseDTO(
                bill.getId(),
                bill.getIssueDate(),
                bill.getTotalAmount(),
                bill.getPaymentStatus(),
                bill.getDueDate()
        );

        return new PaymentResponseDTO(
                payment.getId(),
                payment.getPaymentDate(),
                payment.getAmountPaid(),
                payment.getPaymentMethod(),
                billDTO
        );
    }
}
