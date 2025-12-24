package org.ust.project.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.ust.project.dto.AppointmentResponseDTO;
import org.ust.project.dto.BillResponseDTO;
import org.ust.project.dto.ConsultationRequestDTO;
import org.ust.project.dto.ConsultationResponseDTO;
import org.ust.project.dto.DoctorResponseDTO;
import org.ust.project.dto.PatientResponseDTO;
import org.ust.project.dto.PrescriptionResponseDTO;
import org.ust.project.exception.AppointmentNotFoundException;
import org.ust.project.exception.ConsultationNotFoundException;
import org.ust.project.model.Appointment;
import org.ust.project.model.Consultation;
import org.ust.project.repo.AppointmentRepository;
import org.ust.project.repo.ConsultationRepository;

@Service
@Transactional
public class ConsultationService {

    private final ConsultationRepository consultationRepository;
    private final AppointmentRepository appointmentRepository;
    private final InventoryItemService inventoryItemService;

    public ConsultationService(
            ConsultationRepository consultationRepository,
            AppointmentRepository appointmentRepository,
            InventoryItemService inventoryItemService) {
        this.consultationRepository = consultationRepository;
        this.appointmentRepository = appointmentRepository;
        this.inventoryItemService = inventoryItemService;
    }

    /* ================= CREATE ================= */
    public ConsultationResponseDTO createConsultation(ConsultationRequestDTO dto) {

        Appointment appointment = appointmentRepository.findById(dto.getAppointmentId())
                .orElseThrow(() -> new AppointmentNotFoundException(dto.getAppointmentId()));

        Consultation consultation = new Consultation();
        consultation.setAppointment(appointment);
        consultation.setConsultationDate(dto.getConsultationDate());
        consultation.setReasonForVisit(dto.getReasonForVisit());
        consultation.setNotes(dto.getNotes());

        Consultation saved = consultationRepository.save(consultation);

        return toResponseDTO(saved);
    }

    /* ================= GET BY ID ================= */
    public ConsultationResponseDTO getConsultationById(Long id) {

        Consultation consultation = consultationRepository.findById(id)
                .orElseThrow(() -> new ConsultationNotFoundException(id));

        return toResponseDTO(consultation);
    }

    /* ================= GET ALL ================= */
    public List<ConsultationResponseDTO> getAllConsultations() {

        return consultationRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    /* ================= UPDATE ================= */
    public ConsultationResponseDTO updateConsultation(Long id, ConsultationRequestDTO dto) {

        Consultation consultation = consultationRepository.findById(id)
                .orElseThrow(() -> new ConsultationNotFoundException(id));

        consultation.setConsultationDate(dto.getConsultationDate());
        consultation.setReasonForVisit(dto.getReasonForVisit());
        consultation.setNotes(dto.getNotes());

        Consultation updated = consultationRepository.save(consultation);

        return toResponseDTO(updated);
    }

    /* ================= DELETE ================= */
    public void deleteConsultation(Long id) {

        Consultation consultation = consultationRepository.findById(id)
                .orElseThrow(() -> new ConsultationNotFoundException(id));

        consultationRepository.delete(consultation);
    }

    /* ================= DTO MAPPER ================= */
    private ConsultationResponseDTO toResponseDTO(Consultation consultation) {

        Appointment appointment = consultation.getAppointment();

        AppointmentResponseDTO appointmentDTO = new AppointmentResponseDTO(
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
        );
        
        BillResponseDTO billDTO = consultation.getBill() != null
                ? new BillResponseDTO(
                        consultation.getBill().getId(),
                        consultation.getBill().getIssueDate(),
                        consultation.getBill().getTotalAmount(),
                        consultation.getBill().getPaymentStatus(),
                        consultation.getBill().getDueDate(),
                        null   // ❗ IMPORTANT: avoid circular reference
                )
                : null;


        PrescriptionResponseDTO prescriptionDTO = consultation.getPrescription() != null
                ? new PrescriptionResponseDTO(
                        consultation.getPrescription().getId(),
                        consultation.getPrescription().getMedicationName(),
                        consultation.getPrescription().getDosageMg(),
                        consultation.getPrescription().getPrice(),
                        consultation.getPrescription().getFrequency(),
                        consultation.getPrescription().getStartDate(),
                        consultation.getPrescription().getEndDate(),
                        consultation.getPrescription().getInventoryItems()
                                .stream()
                                .map(item -> new org.ust.project.dto.InventoryItemResponseDTO(
                                        item.getId(),
                                        item.getItemName(),
                                        item.getCategory(),
                                        item.getUnitPrice()
                                ))
                                .collect(Collectors.toList())
                )
                : null;

        return new ConsultationResponseDTO(
                consultation.getId(),
                consultation.getConsultationDate(),
                consultation.getReasonForVisit(),
                consultation.getNotes(),
                appointmentDTO,
                billDTO,
                prescriptionDTO,
                consultation.getConsultationStatus()
        );
    }
}
