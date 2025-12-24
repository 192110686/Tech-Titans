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
import org.ust.project.exception.AppointmentNotFoundException;
import org.ust.project.exception.ConsultationNotFoundException;
import org.ust.project.model.Appointment;
import org.ust.project.model.AppointmentStatus;
import org.ust.project.model.Consultation;
import org.ust.project.repo.AppointmentRepository;
import org.ust.project.repo.ConsultationRepository;

@Service
@Transactional
public class ConsultationService {

    private final ConsultationRepository consultationRepository;
    private final AppointmentRepository appointmentRepository;
    private final BillService billService;  // Autowire BillService to handle bill generation

    public ConsultationService(
            ConsultationRepository consultationRepository,
            AppointmentRepository appointmentRepository,
            BillService billService) {
        this.consultationRepository = consultationRepository;
        this.appointmentRepository = appointmentRepository;
        this.billService = billService;
    }

    /* ================= CREATE ================= */
    public ConsultationResponseDTO createConsultation(ConsultationRequestDTO dto) {

        Appointment appointment = appointmentRepository.findById(dto.getAppointmentId())
                .orElseThrow(() -> new AppointmentNotFoundException(dto.getAppointmentId()));

        // Update appointment status to IN_PROGRESS when consultation starts
        appointment.setStatus(AppointmentStatus.IN_PROGRESS.toString());

        Consultation consultation = new Consultation();
        consultation.setAppointment(appointment);
        consultation.setConsultationDate(dto.getConsultationDate());
        consultation.setReasonForVisit(dto.getReasonForVisit());
        consultation.setNotes(dto.getNotes());

        // Maintain bidirectional mapping
        appointment.setConsultation(consultation);

        Consultation savedConsultation = consultationRepository.save(consultation);

        // Once appointment status changes to COMPLETED, call BillService to generate the bill
        if (appointment.getStatus().equals(AppointmentStatus.COMPLETED.toString())) {
            billService.createBill(savedConsultation.getId()); // Bill generation based on completed consultation
        }

        return toResponseDTO(savedConsultation);
    }

    /* ================= COMPLETE CONSULTATION ================= */
    public void completeConsultation(Long consultationId) {
        Consultation consultation = consultationRepository.findById(consultationId)
                .orElseThrow(() -> new RuntimeException("Consultation not found"));

        Appointment appointment = consultation.getAppointment();

        // Update appointment status to COMPLETED when consultation is finished
        if (appointment.getStatus().equals(AppointmentStatus.IN_PROGRESS.toString())) {
            appointment.setStatus(AppointmentStatus.COMPLETED.toString());
            appointmentRepository.save(appointment);  // Save updated appointment status
        }

        // Call BillService to generate bill after consultation is completed
        billService.createBill(consultationId); // Generate the bill for completed consultation
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

        return new ConsultationResponseDTO(
                consultation.getId(),
                consultation.getConsultationDate(),
                consultation.getReasonForVisit(),
                consultation.getNotes(),
                appointmentDTO,
                billDTO,
                null, // Do not include prescription in this DTO for simplicity
                consultation.getConsultationStatus()
        );
    }
}
