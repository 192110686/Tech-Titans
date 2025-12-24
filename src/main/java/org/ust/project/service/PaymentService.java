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

        Payment payment = new Payment();
        payment.setPaymentDate(dto.getPaymentDate());
        payment.setAmountPaid(dto.getAmountPaid());
        payment.setPaymentMethod(dto.getPaymentMethod());
        payment.setBill(bill);

        Payment savedPayment = paymentRepository.save(payment);

        // Update bill status
        bill.setPaymentStatus("PAID");
        billRepository.save(bill);

        return toResponseDTO(savedPayment);
    }

    /* ================= GET BY ID ================= */
    public PaymentResponseDTO getPaymentById(Long id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new PaymentEntityNotFoundException(id));

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

    /* ================= DELETE ================= */
    public void deletePayment(Long id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new PaymentEntityNotFoundException(id));

        paymentRepository.delete(payment);
    }

    /* ================= DTO MAPPER ================= */
    private PaymentResponseDTO toResponseDTO(Payment payment) {

        Bill bill = payment.getBill();
        Consultation consultation = bill.getConsultation();
        Appointment appointment = consultation.getAppointment();

        return new PaymentResponseDTO(
                payment.getId(),
                payment.getPaymentDate(),
                payment.getAmountPaid(),
                payment.getPaymentMethod(),
                new BillResponseDTO(
                        bill.getId(),
                        bill.getIssueDate(),
                        bill.getTotalAmount(),
                        bill.getPaymentStatus(),
                        bill.getDueDate(),
                        new ConsultationResponseDTO(
                                consultation.getId(),
                                consultation.getConsultationDate(),
                                consultation.getReasonForVisit(),
                                consultation.getNotes(),
                                new AppointmentResponseDTO(
                                        appointment.getId(),
                                        appointment.getAppointmentDate(),
                                        appointment.getReasonForVisit(),
                                        appointment.getStatus(),
                                        appointment.getTimeSlot(),
                                        new DoctorResponseDTO(
                                                appointment.getDoctor().getId(),
                                                appointment.getDoctor().getFirstName(),
                                                appointment.getDoctor().getLastName(),
                                                appointment.getDoctor().getSpecialization(),
                                                appointment.getDoctor().getAvailabilitySchedule()
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
                                null,   // bill (avoid circular reference)
                                null ,   // prescription (optional here)
                                consultation.getConsultationStatus()
                        )
                )
        );

    }
}
