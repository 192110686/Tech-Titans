package org.ust.project.service;

import org.ust.project.dto.PaymentRequestDTO;
import org.ust.project.dto.PaymentResponseDTO;
import org.ust.project.dto.BillResponseDTO;
import org.ust.project.model.Payment;
import org.ust.project.model.Bill;
import org.ust.project.repo.PaymentRepository;
import org.ust.project.repo.BillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private BillRepository billRepository;

    // Create a new payment
    public PaymentResponseDTO createPayment(PaymentRequestDTO paymentRequestDTO) {
        Payment payment = new Payment();
        payment.setPaymentDate(paymentRequestDTO.getPaymentDate());
        payment.setAmountPaid(paymentRequestDTO.getAmountPaid());
        payment.setPaymentMethod(paymentRequestDTO.getPaymentMethod());

        // Fetch the Bill object using billId from the DTO
        Optional<Bill> billOptional = billRepository.findById(paymentRequestDTO.getBillId());
        if (billOptional.isPresent()) {
            payment.setBill(billOptional.get());
            payment = paymentRepository.save(payment);

            // Return a response DTO including the Bill details
            return new PaymentResponseDTO(
                    payment.getId(),
                    payment.getPaymentDate(),
                    payment.getAmountPaid(),
                    payment.getPaymentMethod(),
                    new BillResponseDTO(
                        payment.getBill().getId(),
                        payment.getBill().getIssueDate(),
                        payment.getBill().getTotalAmount(),
                        payment.getBill().getPaymentStatus(),
                        payment.getBill().getDueDate()
                    )
            );
        }
        return null; // Or throw an exception if bill not found
    }

    // Get payment by ID
    public PaymentResponseDTO getPaymentById(Long id) {
        Optional<Payment> paymentOptional = paymentRepository.findById(id);
        if (paymentOptional.isPresent()) {
            Payment payment = paymentOptional.get();
            return new PaymentResponseDTO(
                    payment.getId(),
                    payment.getPaymentDate(),
                    payment.getAmountPaid(),
                    payment.getPaymentMethod(),
                    new BillResponseDTO(
                        payment.getBill().getId(),
                        payment.getBill().getIssueDate(),
                        payment.getBill().getTotalAmount(),
                        payment.getBill().getPaymentStatus(),
                        payment.getBill().getDueDate()
                    )
            );
        }
        return null; // Or throw an exception if payment not found
    }

    // Get all payments
    public List<PaymentResponseDTO> getAllPayments() {
        List<Payment> payments = paymentRepository.findAll();
        return payments.stream()
                .map(payment -> new PaymentResponseDTO(
                        payment.getId(),
                        payment.getPaymentDate(),
                        payment.getAmountPaid(),
                        payment.getPaymentMethod(),
                        new BillResponseDTO(
                            payment.getBill().getId(),
                            payment.getBill().getIssueDate(),
                            payment.getBill().getTotalAmount(),
                            payment.getBill().getPaymentStatus(),
                            payment.getBill().getDueDate()
                        )
                ))
                .collect(Collectors.toList());
    }

    // Update payment details
    public PaymentResponseDTO updatePayment(Long id, PaymentRequestDTO paymentRequestDTO) {
        Optional<Payment> paymentOptional = paymentRepository.findById(id);
        if (paymentOptional.isPresent()) {
            Payment payment = paymentOptional.get();
            payment.setPaymentDate(paymentRequestDTO.getPaymentDate());
            payment.setAmountPaid(paymentRequestDTO.getAmountPaid());
            payment.setPaymentMethod(paymentRequestDTO.getPaymentMethod());

            // Fetch the Bill object using billId from the DTO
            Optional<Bill> billOptional = billRepository.findById(paymentRequestDTO.getBillId());
            if (billOptional.isPresent()) {
                payment.setBill(billOptional.get());
                payment = paymentRepository.save(payment);

                return new PaymentResponseDTO(
                        payment.getId(),
                        payment.getPaymentDate(),
                        payment.getAmountPaid(),
                        payment.getPaymentMethod(),
                        new BillResponseDTO(
                            payment.getBill().getId(),
                            payment.getBill().getIssueDate(),
                            payment.getBill().getTotalAmount(),
                            payment.getBill().getPaymentStatus(),
                            payment.getBill().getDueDate()
                        )
                );
            }
        }
        return null; // Or throw an exception if payment not found or bill not found
    }

    // Delete payment
    public boolean deletePayment(Long id) {
        if (paymentRepository.existsById(id)) {
            paymentRepository.deleteById(id);
            return true;
        }
        return false; // Or throw an exception if payment not found
    }
}
