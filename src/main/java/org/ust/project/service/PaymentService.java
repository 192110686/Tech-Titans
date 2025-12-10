package org.ust.project.service;

import org.ust.project.model.Bill;
import org.ust.project.model.Payment;
import org.ust.project.repo.BillRepository;
import org.ust.project.repo.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private BillRepository billRepository;

    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    public Optional<Payment> getPaymentById(Long id) {
        return paymentRepository.findById(id);
    }

    // Link payment to a specific Bill
    public Payment createPayment(Long billId, Payment payment) {
        Bill bill = billRepository.findById(billId)
                .orElseThrow(() -> new RuntimeException("Bill not found with ID: " + billId));
        
        payment.setBill(bill);
        payment.setPaymentDate(java.time.LocalDate.now());
        
        // Optional: Auto-update bill status to 'Paid'
        bill.setPaymentStatus("Paid");
        billRepository.save(bill);

        return paymentRepository.save(payment);
    }

    public void deletePayment(Long id) {
        paymentRepository.deleteById(id);
    }
}