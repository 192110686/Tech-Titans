package org.ust.project.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.ust.project.dto.*;
import org.ust.project.exception.BillNotFoundException;
import org.ust.project.exception.PaymentEntityNotFoundException;
import org.ust.project.model.*;
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
                Consultation consultation1=bill.getConsultation();
                Appointment appointment1 = consultation1.getAppointment();
                appointment1.setStatus("COMPLETED");

        Payment payment = new Payment();
        payment.setPaymentDate(dto.getPaymentDate());
        payment.setAmountPaid(dto.getAmountPaid());
        payment.setPaymentMethod(dto.getPaymentMethod());

        // ✅ SET BOTH SIDES
        payment.setBill(bill);
        bill.setPayment(payment);

        Payment savedPayment = paymentRepository.save(payment);

        // Ensure the Consultation and Appointment exist before mapping
        Consultation consultation = bill.getConsultation();
        if (consultation == null) {
            throw new RuntimeException("Consultation not found for the bill.");
        }

        Appointment appointment = consultation.getAppointment();
        if (appointment == null) {
            throw new RuntimeException("Appointment not found for the consultation.");
        }

        // Update the bill status based on payment amount
        double totalAmount = bill.getTotalAmount();
        double amountPaid = savedPayment.getAmountPaid();

        if (amountPaid >= totalAmount) {
            bill.setPaymentStatus("PAID"); // Full payment
        } else {
            bill.setPaymentStatus("PARTIALLY PAID"); // Partial payment
        }

        billRepository.save(bill);

        return toResponseDTO(savedPayment, consultation, appointment);
    }

    /* ================= GET BY ID ================= */
    public PaymentResponseDTO getPaymentById(Long id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new PaymentEntityNotFoundException(id));

        // Fetch the Consultation and Appointment
        Bill bill = payment.getBill();
        Consultation consultation = bill.getConsultation();
        if (consultation == null) {
            throw new RuntimeException("Consultation not found for the bill.");
        }
        Appointment appointment = consultation.getAppointment();
        if (appointment == null) {
            throw new RuntimeException("Appointment not found for the consultation.");
        }

        return toResponseDTO(payment, consultation, appointment);
    }

    /* ================= GET ALL ================= */
    public List<PaymentResponseDTO> getAllPayments() {
        return paymentRepository.findAll()
                .stream()
                .map(payment -> {
                    // Fetch Consultation and Appointment for each Payment
                    Bill bill = payment.getBill();
                    Consultation consultation = bill.getConsultation();
                    if (consultation == null) {
                        throw new RuntimeException("Consultation not found for the bill.");
                    }
                    Appointment appointment = consultation.getAppointment();
                    if (appointment == null) {
                        throw new RuntimeException("Appointment not found for the consultation.");
                    }
                    return toResponseDTO(payment, consultation, appointment);
                })
                .collect(Collectors.toList());
    }

    /* ================= UPDATE ================= */
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

        // Fetch Consultation and Appointment
        Consultation consultation = bill.getConsultation();
        if (consultation == null) {
            throw new RuntimeException("Consultation not found for the bill.");
        }

        Appointment appointment = consultation.getAppointment();
        if (appointment == null) {
            throw new RuntimeException("Appointment not found for the consultation.");
        }

        // Update the bill status based on payment amount
        double totalAmount = bill.getTotalAmount();
        double amountPaid = updatedPayment.getAmountPaid();

        if (amountPaid >= totalAmount) {
            bill.setPaymentStatus("PAID");
        } else {
            bill.setPaymentStatus("PARTIALLY PAID");
        }

        billRepository.save(bill);

        return toResponseDTO(updatedPayment, consultation, appointment);
    }

    /* ================= DELETE ================= */
    public void deletePayment(Long id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new PaymentEntityNotFoundException(id));

        paymentRepository.delete(payment);
    }

    /* ================= DTO MAPPER ================= */
    private PaymentResponseDTO toResponseDTO(Payment payment, Consultation consultation, Appointment appointment) {

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
                        payment.getBill().getDueDate(),
                        new ConsultationResponseDTO(
                                consultation.getId(),
                                consultation.getConsultationDate(),
                                consultation.getReasonForVisit(),
                                consultation.getNotes(),
                                new AppointmentResponseDTO(
                                        appointment.getId(),
                                        appointment.getAppointmentDateTime(),
                                        appointment.getStatus(),
                                        appointment.getReasonForVisit(),
                                        new DoctorResponseDTO(
                                                appointment.getDoctor().getId(),
                                                appointment.getDoctor().getFirstName(),
                                                appointment.getDoctor().getLastName(),
                                                appointment.getDoctor().getSpecialization(),
                                                appointment.getDoctor().getAvailableSchedule()
                                        ),
                                        new PatientResponseDTO(
                                                appointment.getPatient().getId(),
                                                appointment.getPatient().getFirstName(),
                                                appointment.getPatient().getLastName(),
                                                appointment.getPatient().getDateOfBirth(),
                                                appointment.getPatient().getGender(),
                                                appointment.getPatient().getPhoneNumber(),
                                                appointment.getPatient().getEmail(),
                                                appointment.getPatient().getBloodGroup()
                                        )
                                ),
                                null,   // Avoid circular reference (we don't need to send bill back)
                                null,   // Prescription (optional, and may not be needed in payment response)
                                consultation.getConsultationStatus()
                        )
                )
        );
    }
}
