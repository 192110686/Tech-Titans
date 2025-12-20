package org.ust.project.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.ust.project.dto.AppointmentResponseDTO;
import org.ust.project.dto.BillResponseDTO;
import org.ust.project.dto.DoctorResponseDTO;
import org.ust.project.dto.PatientResponseDTO;
import org.ust.project.dto.PaymentRequestDTO;
import org.ust.project.dto.PaymentResponseDTO;
import org.ust.project.exception.BillNotFoundException;
import org.ust.project.exception.PaymentEntityNotFoundException;
import org.ust.project.model.Bill;
import org.ust.project.model.Payment;
import org.ust.project.repo.BillRepository;
import org.ust.project.repo.PaymentRepository;

@Service
@Transactional
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private BillRepository billRepository;

    /* ================= CREATE ================= */
    public PaymentResponseDTO createPayment(PaymentRequestDTO dto) {

        Bill bill = billRepository.findById(dto.getBillId())
            .orElseThrow(() -> new BillNotFoundException(dto.getBillId()));

        Payment payment = new Payment();
        payment.setPaymentDate(dto.getPaymentDate());
        payment.setAmountPaid(dto.getAmountPaid());
        payment.setPaymentMethod(dto.getPaymentMethod());
        payment.setBill(bill);

        // Save the payment first
        Payment savedPayment = paymentRepository.save(payment);

        // Update the bill's payment status after saving the payment
        bill.setPaymentStatus("PAID");
        billRepository.save(bill);  // Ensure the bill is updated with the status

        // Return the response DTO with the full Bill information
        return toResponseDTO(savedPayment);
    }

    /* ================= GET PAYMENT BY ID ================= */
    public PaymentResponseDTO getPaymentById(Long id) {
        Payment payment = paymentRepository.findById(id)
            .orElseThrow(() -> new PaymentEntityNotFoundException(id));

        return toResponseDTO(payment);
    }

    /* ================= GET ALL PAYMENTS ================= */
    public List<PaymentResponseDTO> getAllPayments() {
        return paymentRepository.findAll()
            .stream()
            .map(this::toResponseDTO)
            .collect(Collectors.toList());
    }

    /* ================= UPDATE PAYMENT ================= */
    public PaymentResponseDTO updatePayment(Long id, PaymentRequestDTO dto) {

        Payment payment = paymentRepository.findById(id)
            .orElseThrow(() -> new PaymentEntityNotFoundException(id));

        Bill bill = billRepository.findById(dto.getBillId())
            .orElseThrow(() -> new BillNotFoundException(dto.getBillId()));

        payment.setPaymentDate(dto.getPaymentDate());
        payment.setAmountPaid(dto.getAmountPaid());
        payment.setPaymentMethod(dto.getPaymentMethod());
        payment.setBill(bill);

        Payment updatedPayment = paymentRepository.save(payment);

        return toResponseDTO(updatedPayment);
    }

    /* ================= DELETE PAYMENT ================= */
    public void deletePayment(Long id) {
        Payment payment = paymentRepository.findById(id)
            .orElseThrow(() -> new PaymentEntityNotFoundException(id));

        paymentRepository.delete(payment);
    }

    /* ================= DTO MAPPER ================= */
    private PaymentResponseDTO toResponseDTO(Payment payment) {

        Bill bill = payment.getBill();  // Ensure the bill is fetched with all its details
        bill.getAppointment().getId();
        BillResponseDTO billDTO = new BillResponseDTO(
            bill.getId(),
            bill.getIssueDate(),
            bill.getTotalAmount(),
            bill.getPaymentStatus(),
            bill.getDueDate(),
            // Create AppointmentResponseDTO for the bill's appointment
            new AppointmentResponseDTO(
                bill.getAppointment().getId(),
                bill.getAppointment().getAppointmentDate(),
                // Add other fields like doctor and patient details if needed
                new DoctorResponseDTO(
                    bill.getAppointment().getDoctor().getId(),
                    bill.getAppointment().getDoctor().getFirstName(),
                    bill.getAppointment().getDoctor().getLastName(),
                    bill.getAppointment().getDoctor().getSpecialization(),
                    bill.getAppointment().getDoctor().getAvailabilitySchedule()
                ),
                new PatientResponseDTO(
                    bill.getAppointment().getPatient().getId(),
                    bill.getAppointment().getPatient().getFirstName(),
                    bill.getAppointment().getPatient().getLastName(),
                    bill.getAppointment().getPatient().getDateOfBirth(),
                    bill.getAppointment().getPatient().getGender(),
                    bill.getAppointment().getPatient().getPhoneNumber(),
                    bill.getAppointment().getPatient().getEmail(),
                    bill.getAppointment().getPatient().getBloodGroup()
                )
            )
        );

        return new PaymentResponseDTO(
            payment.getId(),
            payment.getPaymentDate(),
            payment.getAmountPaid(),
            payment.getPaymentMethod(),
            billDTO  // Include the full Bill in the response
        );
    }
}
