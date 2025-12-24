package org.ust.project.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
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

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ConsultationService {

    private final ConsultationRepository consultationRepository;
    private final AppointmentRepository appointmentRepository;

    public ConsultationService(
            ConsultationRepository consultationRepository,
            AppointmentRepository appointmentRepository) {
        this.consultationRepository = consultationRepository;
        this.appointmentRepository = appointmentRepository;
    }

    /* ================= CREATE ================= */
    public ConsultationResponseDTO createConsultation(ConsultationRequestDTO dto) {

        Appointment appointment = appointmentRepository.findById(dto.getAppointmentId())
                .orElseThrow(() -> new AppointmentNotFoundException(dto.getAppointmentId()));

        // 🔹 Update appointment status when consultation starts
        appointment.setStatus("IN_PROGRESS");

        Consultation consultation = new Consultation();
        consultation.setAppointment(appointment);
        consultation.setConsultationDate(dto.getConsultationDate());
        consultation.setReasonForVisit(dto.getReasonForVisit());
        consultation.setNotes(dto.getNotes());

        // 🔹 Maintain bidirectional mapping
        appointment.setConsultation(consultation);

        Consultation saved = consultationRepository.save(consultation);

        return toResponseDTO(saved);
    }

    /* ================= COMPLETE CONSULTATION ================= */
    public ConsultationResponseDTO completeConsultation(Long consultationId) {

        Consultation consultation = consultationRepository.findById(consultationId)
                .orElseThrow(() -> new ConsultationNotFoundException(consultationId));

        Appointment appointment = consultation.getAppointment();
        appointment.setStatus("COMPLETED");

        return toResponseDTO(consultation);
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

        return toResponseDTO(consultation);
    }

    /* ================= DELETE ================= */
    public void deleteConsultation(Long id) {

        Consultation consultation = consultationRepository.findById(id)
                .orElseThrow(() -> new ConsultationNotFoundException(id));

        consultationRepository.delete(consultation);
    }

    /* ================= DTO MAPPER ================= */
    private ConsultationResponseDTO toResponseDTO(Consultation consultation) {

        Appointment appt = consultation.getAppointment();

        // Appointment mapping
        AppointmentResponseDTO appointmentDTO = appt == null ? null : new AppointmentResponseDTO(
                appt.getId(),
                appt.getAppointmentDate(),
                appt.getReasonForVisit(),
                appt.getStatus(),
                appt.getTimeSlot(),
                new DoctorResponseDTO(
                        appt.getDoctor().getId(),
                        appt.getDoctor().getFirstName(),
                        appt.getDoctor().getLastName(),
                        appt.getDoctor().getSpecialization(),
                        appt.getDoctor().getAvailabilitySchedule()
                ),
                new PatientResponseDTO(
                        appt.getPatient().getId(),
                        appt.getPatient().getFirstName(),
                        appt.getPatient().getLastName(),
                        appt.getPatient().getDateOfBirth(),
                        appt.getPatient().getGender(),
                        appt.getPatient().getPhoneNumber(),
                        appt.getPatient().getEmail(),
                        appt.getPatient().getBloodGroup()
                )
        );

        // Bill mapping - check if bill is null
        BillResponseDTO billDTO = (consultation.getBill() != null) ? new BillResponseDTO(
                consultation.getBill().getId(),
                consultation.getBill().getIssueDate(),
                consultation.getBill().getTotalAmount(),
                consultation.getBill().getPaymentStatus(),
                consultation.getBill().getDueDate(),
                null // avoid circular reference
        ) : null;

        // Prescription mapping - check if prescription is null
        PrescriptionResponseDTO prescriptionDTO = (consultation.getPrescription() != null) ? new PrescriptionResponseDTO(
                consultation.getPrescription().getId(),
                consultation.getPrescription().getMedicationName(),
                consultation.getPrescription().getDosageMg(),
                consultation.getPrescription().getPrice(),
                consultation.getPrescription().getFrequency(),
                consultation.getPrescription().getStartDate(),
                consultation.getPrescription().getEndDate(),
                null // avoiding circular reference
        ) : null;

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
