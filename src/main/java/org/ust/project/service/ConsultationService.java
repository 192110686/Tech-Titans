package org.ust.project.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.ust.project.dto.*;
import org.ust.project.exception.AppointmentNotFoundException;
import org.ust.project.exception.ConsultationNotFoundException;
import org.ust.project.model.*;
import org.ust.project.repo.AppointmentRepository;
import org.ust.project.repo.ConsultationRepository;

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

        // Update appointment status to IN_PROGRESS when consultation starts
        appointment.setStatus("IN_PROGRESS");

        Consultation consultation = new Consultation();
        consultation.setAppointment(appointment);
        consultation.setConsultationDate(dto.getConsultationDate());
        consultation.setReasonForVisit(dto.getReasonForVisit());
        consultation.setNotes(dto.getNotes());

        // Maintain bidirectional mapping
        appointment.setConsultation(consultation);

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

        return toResponseDTO(consultation);
    }

    /* ================= DELETE ================= */
    public void deleteConsultation(Long id) {
        Consultation consultation = consultationRepository.findById(id)
                .orElseThrow(() -> new ConsultationNotFoundException(id));

        consultationRepository.delete(consultation);
    }

    /* ================= START CONSULTATION ================= */
    public void startConsultation(Long consultationId) {
        Consultation consultation = consultationRepository.findById(consultationId)
                .orElseThrow(() -> new RuntimeException("Consultation not found"));

        // Update appointment status to IN_PROGRESS when consultation starts
        consultation.getAppointment().setStatus("IN_PROGRESS");
        appointmentRepository.save(consultation.getAppointment()); // Save updated appointment status
    }

    /* ================= COMPLETE CONSULTATION ================= */
    public void completeConsultation(Long consultationId) {
        Consultation consultation = consultationRepository.findById(consultationId)
                .orElseThrow(() -> new RuntimeException("Consultation not found"));

        // Update appointment status to COMPLETED when consultation is finished
        consultation.getAppointment().setStatus("COMPLETED");
        appointmentRepository.save(consultation.getAppointment()); // Save updated appointment status
    }

    /* ================= DTO MAPPER ================= */
    private ConsultationResponseDTO toResponseDTO(Consultation consultation) {

        Appointment appt = consultation.getAppointment();

        // Appointment mapping
        AppointmentResponseDTO appointmentDTO = appt == null ? null : new AppointmentResponseDTO(
                appt.getId(),
                appt.getAppointmentDateTime(),
                appt.getReasonForVisit(),
                appt.getStatus(),
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
