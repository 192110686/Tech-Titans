package org.ust.project.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.ust.project.dto.AppointmentRequestDTO;
import org.ust.project.dto.AppointmentResponseDTO;
import org.ust.project.dto.DoctorResponseDTO;
import org.ust.project.dto.PatientResponseDTO;
import org.ust.project.exception.AppointmentNotFoundException;
import org.ust.project.exception.DoctorEntityNotFoundException;
import org.ust.project.exception.PatientEntityNotFoundException;
import org.ust.project.model.Appointment;
import org.ust.project.model.AppointmentStatus;
import org.ust.project.model.Doctor;
import org.ust.project.model.Patient;
import org.ust.project.repo.AppointmentRepository;
import org.ust.project.repo.DoctorRepository;
import org.ust.project.repo.PatientRepository;

@Service
@Transactional
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final DoctorService doctorService;

    public AppointmentService(
            AppointmentRepository appointmentRepository,
            DoctorRepository doctorRepository,
            PatientRepository patientRepository, 
            DoctorService doctorService) {
        this.appointmentRepository = appointmentRepository;
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
        this.doctorService = doctorService;
    }

    /* ================= CREATE ================= */
    public AppointmentResponseDTO createAppointment(AppointmentRequestDTO dto) {

        // Fetch Doctor by ID
        Doctor doctor = doctorRepository.findById(dto.getDoctorId())
                .orElseThrow(() -> new DoctorEntityNotFoundException(dto.getDoctorId()));

        // Fetch Patient by ID using the patientId in the DTO
        Patient patient = patientRepository.findById(dto.getPatientId())
                .orElseThrow(() -> new PatientEntityNotFoundException(dto.getPatientId()));


// Prevent double booking
boolean alreadyBooked = appointmentRepository
        .existsByDoctorIdAndAppointmentDateAndTimeSlot(
                doctor.getId(),
                dto.getAppointmentDateTime(),
                dto.getTimeSlot()
        );


        if (alreadyBooked) {
            throw new IllegalStateException("Doctor is already booked for this time slot");
        }

        // Create a new appointment and set the necessary fields
        Appointment appointment = new Appointment();
        appointment.setDoctor(doctor);
        appointment.setPatient(patient);  // Set the fetched Patient entity
        appointment.setAppointmentDateTime(dto.getAppointmentDateTime()); // Use LocalDateTime
        appointment.setReasonForVisit(dto.getReasonForVisit());
        appointment.setStatus(dto.getStatus()); // SCHEDULED / CANCELLED / COMPLETED

        // Save the appointment and return the response
        return toResponseDTO(appointmentRepository.save(appointment));
    }

    /* ================= GET BY ID ================= */
    public AppointmentResponseDTO getAppointmentById(Long id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new AppointmentNotFoundException(id));

        return toResponseDTO(appointment);
    }

    /* ================= GET ALL ================= */
    public List<AppointmentResponseDTO> getAllAppointments() {
        return appointmentRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    /* ================= UPDATE ================= */
    public AppointmentResponseDTO updateAppointment(Long id, AppointmentRequestDTO dto) {

        // Fetch the existing appointment by ID
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new AppointmentNotFoundException(id));

        // Fetch Doctor and Patient from database
        Doctor doctor = doctorRepository.findById(dto.getDoctorId())
                .orElseThrow(() -> new DoctorEntityNotFoundException(dto.getDoctorId()));

        Patient patient = patientRepository.findById(dto.getPatientId())
                .orElseThrow(() -> new PatientEntityNotFoundException(dto.getPatientId()));

        // Update appointment fields
        appointment.setDoctor(doctor);
        appointment.setPatient(patient); // Set the fetched Patient entity
        appointment.setAppointmentDateTime(dto.getAppointmentDateTime()); // Use LocalDateTime
        appointment.setReasonForVisit(dto.getReasonForVisit());
        appointment.setStatus(dto.getStatus());

        // Save and return the updated appointment
        return toResponseDTO(appointmentRepository.save(appointment));
    }

    /* ================= DELETE ================= */
    public void deleteAppointment(Long id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new AppointmentNotFoundException(id));

        appointmentRepository.delete(appointment);
    }

    /* ========================== Doctor Availability ========================= */
    // Method to check if the doctor is available at the requested time
   public boolean checkDoctorAvailability(Long doctorId, LocalDateTime requestedTime) {
    // Fetch the Doctor entity directly using the DoctorRepository (no need to rely on DoctorDTO)
    Doctor doctor = doctorRepository.findById(doctorId)
            .orElseThrow(() -> new DoctorEntityNotFoundException(doctorId)); // Use DoctorRepository to fetch entity

    // Check if the doctor already has an appointment at the requested time
    List<Appointment> existingAppointments = appointmentRepository
            .findByDoctorAndAppointmentDateTime(doctor, requestedTime);

    // If no appointments, doctor is available
    return existingAppointments.isEmpty();
}


    /* ============================ Book the Appointment if Doctor is Available =================== */
    public String bookAppointment(AppointmentRequestDTO appointmentRequest) {
        // Check if the doctor is available
        boolean isAvailable = checkDoctorAvailability(appointmentRequest.getDoctorId(), appointmentRequest.getAppointmentDateTime());

        if (!isAvailable) {
            // If the doctor is not available, get the available time slots
            List<LocalDateTime> availableSlots = doctorService.getAvailableSlots(
                    appointmentRequest.getDoctorId(),
                    appointmentRequest.getAppointmentDateTime().minusHours(1),
                    appointmentRequest.getAppointmentDateTime().plusHours(1)
            );

            // Return the available slots to the patient
            return "Doctor is not available at the requested time. Available slots: " + availableSlots.toString();
        }

        // If the doctor is available, proceed to book the appointment
        Appointment appointment = new Appointment();

        // Fetch the Patient and Doctor from the database using their IDs
        Patient patient = patientRepository.findById(appointmentRequest.getPatientId())
                .orElseThrow(() -> new PatientEntityNotFoundException(appointmentRequest.getPatientId()));

        Doctor doctor = doctorRepository.findById(appointmentRequest.getDoctorId())
                .orElseThrow(() -> new DoctorEntityNotFoundException(appointmentRequest.getDoctorId()));

        // Set the Patient and Doctor in the Appointment
        appointment.setPatient(patient);
        appointment.setDoctor(doctor);

        // Set the rest of the fields
        appointment.setAppointmentDateTime(appointmentRequest.getAppointmentDateTime()); // Set LocalDateTime
        appointment.setStatus("SCHEDULED"); // Default status is SCHEDULED

        // Save the appointment to the repository
        appointmentRepository.save(appointment);

        return "Appointment booked successfully!";
    }

    // Update status of the appointment (e.g., when consultation starts or completes)
   public void updateAppointmentStatus(Long appointmentId, AppointmentStatus status) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        // Convert the AppointmentStatus enum to String before setting
        appointment.setStatus(status.toString()); // Convert enum to string

        appointmentRepository.save(appointment);
    }

    // Reschedule an appointment by changing the appointment date/time and status
     public void rescheduleAppointment(Long appointmentId, LocalDateTime newDateTime) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        // Check if the new time is available (we'll add the availability check here)
        appointment.setAppointmentDateTime(newDateTime);
        appointment.setStatus(" RESCHEDULED"); // Set status to RESCHEDULED

        appointmentRepository.save(appointment);
    }


    /* ================= DTO MAPPER ================= */
    private AppointmentResponseDTO toResponseDTO(Appointment appointment) {
        return new AppointmentResponseDTO(
                appointment.getId(),
                appointment.getAppointmentDateTime(), // Use appointmentDateTime here
                appointment.getReasonForVisit(),
                appointment.getStatus(),
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
    }
}
