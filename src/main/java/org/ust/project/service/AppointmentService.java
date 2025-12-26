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
                .existsByDoctorIdAndAppointmentDateTime(
                        doctor.getId(),
                        dto.getAppointmentDateTime()
                );

        if (alreadyBooked) {
            throw new IllegalStateException("Doctor is already booked for this time slot");
        }

        // Create a new appointment and set the necessary fields
        Appointment appointment = new Appointment();
        appointment.setDoctor(doctor);
        appointment.setPatient(patient);
        appointment.setAppointmentDateTime(dto.getAppointmentDateTime());
        appointment.setReasonForVisit(dto.getReasonForVisit());
        appointment.setStatus("SCHEDULED"); // Default status as String

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
        appointment.setPatient(patient);
        appointment.setAppointmentDateTime(dto.getAppointmentDateTime());
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

    /* ================= Doctor Availability ================= */
    // Method to check if the doctor is available at the requested time
    public boolean checkDoctorAvailability(Long doctorId, LocalDateTime requestedTime) {
        // Fetch the Doctor entity
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new DoctorEntityNotFoundException(doctorId));

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
            // If doctor is not available, get available time slots
            List<LocalDateTime> availableSlots = doctorService.getAvailableSlots(
                    appointmentRequest.getDoctorId(),
                    appointmentRequest.getAppointmentDateTime().minusHours(1),
                    appointmentRequest.getAppointmentDateTime().plusHours(1)
            );

            // Return available slots to the patient
            return "Doctor is not available at the requested time. Available slots: " + availableSlots.toString();
        }

        // Proceed to book the appointment
        Appointment appointment = new Appointment();

        // Fetch Patient and Doctor from DB
        Patient patient = patientRepository.findById(appointmentRequest.getPatientId())
                .orElseThrow(() -> new PatientEntityNotFoundException(appointmentRequest.getPatientId()));

        Doctor doctor = doctorRepository.findById(appointmentRequest.getDoctorId())
                .orElseThrow(() -> new DoctorEntityNotFoundException(appointmentRequest.getDoctorId()));

        // Set patient, doctor, and other fields
        appointment.setPatient(patient);
        appointment.setDoctor(doctor);
        appointment.setAppointmentDateTime(appointmentRequest.getAppointmentDateTime()); // Set time
        appointment.setStatus("SCHEDULED"); // Set status to SCHEDULED

        appointmentRepository.save(appointment);

        return "Appointment booked successfully!";
    }

    // Update the appointment status
    public void updateAppointmentStatus(Long appointmentId, String status) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        // Set the status as a string
        appointment.setStatus(status);

        appointmentRepository.save(appointment);
    }

    // Reschedule appointment by updating time and status
    public void rescheduleAppointment(Long appointmentId, LocalDateTime newDateTime) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        // Check availability of the new time (you can implement availability check here as well)
        appointment.setAppointmentDateTime(newDateTime);

        appointment.setStatus("RESCHEDULED");
 // Set status to RESCHEDULED


        appointmentRepository.save(appointment);
    }

    /* ================= DTO MAPPER ================= */
    private AppointmentResponseDTO toResponseDTO(Appointment appointment) {
        return new AppointmentResponseDTO(
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
        );
    }
}
