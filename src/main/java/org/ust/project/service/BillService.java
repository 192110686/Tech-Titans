package org.ust.project.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.ust.project.dto.*;
import org.ust.project.exception.BillNotFoundException;
import org.ust.project.exception.ConsultationNotFoundException;
import org.ust.project.model.*;
import org.ust.project.repo.BillRepository;
import org.ust.project.repo.ConsultationRepository;

@Service
@Transactional
public class BillService {

    @Autowired
    private BillRepository billRepository;

    @Autowired
    private ConsultationRepository consultationRepository;

    /* ================= CREATE ================= */
    public BillResponseDTO createBill(BillRequestDTO dto) {

        Consultation consultation = consultationRepository.findById(dto.getConsultationId())
                .orElseThrow(() -> new ConsultationNotFoundException(dto.getConsultationId()));

        Bill bill = new Bill();
        bill.setIssueDate(dto.getIssueDate());
        bill.setTotalAmount(dto.getTotalAmount());
        bill.setDueDate(dto.getDueDate());
        bill.setPaymentStatus("UNPAID");
        bill.setConsultation(consultation);

        // bidirectional safety
        consultation.setBill(bill);

        Bill savedBill = billRepository.save(bill);

        return toResponseDTO(savedBill);
    }

    /* ================= GET BY ID ================= */
    public BillResponseDTO getBillById(Long id) {

        Bill bill = billRepository.findById(id)
                .orElseThrow(() -> new BillNotFoundException(id));

        return toResponseDTO(bill);
    }

    /* ================= GET ALL ================= */
    public List<BillResponseDTO> getAllBills() {

        return billRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    /* ================= UPDATE ================= */
    public BillResponseDTO updateBill(Long id, BillRequestDTO dto) {

        Bill bill = billRepository.findById(id)
                .orElseThrow(() -> new BillNotFoundException(id));

        bill.setIssueDate(dto.getIssueDate());
        bill.setTotalAmount(dto.getTotalAmount());
        bill.setDueDate(dto.getDueDate());

        Bill updatedBill = billRepository.save(bill);

        return toResponseDTO(updatedBill);
    }

    /* ================= DELETE ================= */
    public void deleteBill(Long id) {

        Bill bill = billRepository.findById(id)
                .orElseThrow(() -> new BillNotFoundException(id));

        billRepository.delete(bill);
    }

    /* ================= DTO MAPPER ================= */
    private BillResponseDTO toResponseDTO(Bill bill) {

        Consultation consultation = bill.getConsultation();
        Appointment appointment = consultation.getAppointment();

        // Extract time from LocalDateTime (appointmentDateTime)
        String timeSlot = appointment.getAppointmentDateTime().format(DateTimeFormatter.ofPattern("HH:mm"));

        return new BillResponseDTO(
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
                                appointment.getAppointmentDateTime(),
                                appointment.getReasonForVisit(),
                                appointment.getStatus(),
                                timeSlot, // Use the formatted time here
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
                        null,   // bill (avoid circular ref)
                        null,    // prescription (optional)
                        consultation.getConsultationStatus()
                )
        );
    }
}
