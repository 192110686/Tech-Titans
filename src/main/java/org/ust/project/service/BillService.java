package org.ust.project.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.ust.project.dto.AppointmentResponseDTO;
import org.ust.project.dto.BillResponseDTO;
import org.ust.project.dto.ConsultationResponseDTO;
import org.ust.project.dto.DoctorResponseDTO;
import org.ust.project.dto.PatientResponseDTO;
import org.ust.project.exception.BillNotFoundException;
import org.ust.project.exception.ConsultationNotFoundException;
import org.ust.project.model.Appointment;
import org.ust.project.model.Bill;
import org.ust.project.model.Consultation;
import org.ust.project.repo.BillRepository;
import org.ust.project.repo.ConsultationRepository;

@Service
@Transactional
public class BillService {

    @Autowired
    private BillRepository billRepository;

    @Autowired
    private ConsultationRepository consultationRepository;

    /* ================= CREATE BILL ================= */
    public BillResponseDTO createBill(Long consultationId) {

        Consultation consultation = consultationRepository.findById(consultationId)
                .orElseThrow(() -> new ConsultationNotFoundException(consultationId));

        
        // Calculate total amount (consultation fee + prescription fee)
        double totalAmount = 300.0; // Fixed consultation fee

        if (consultation.getPrescription() != null) {
            totalAmount += consultation.getPrescription().getPrice();
        }

        Bill bill = new Bill();
        bill.setIssueDate(LocalDate.now());
        bill.setDueDate(LocalDate.now().plusDays(30));  // Bill due date is 30 days from today
        bill.setTotalAmount(totalAmount);
        bill.setPaymentStatus("UNPAID");
        consultation.setBill(bill);
        bill.setConsultation(consultation);

        Bill savedBill = billRepository.save(bill);

        return toResponseDTO(savedBill);
    }

    /* ================= GET BILL BY ID ================= */
    public BillResponseDTO getBillById(Long id) {
        Bill bill = billRepository.findById(id)
                .orElseThrow(() -> new BillNotFoundException(id));

        return toResponseDTO(bill);
    }

    /* ================= GET ALL BILLS ================= */
    public List<BillResponseDTO> getAllBills() {
        return billRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

   
   

    /* ================= DTO MAPPER ================= */
    private BillResponseDTO toResponseDTO(Bill bill) {
         Consultation consultation = bill.getConsultation();
        Appointment appointment = consultation.getAppointment();
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
                        null,   // bill (avoid circular ref)
                        null   // prescription (optional)
                )
        );
    }
}
