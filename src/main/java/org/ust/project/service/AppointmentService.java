package org.ust.project.service;

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

    public AppointmentService(
            AppointmentRepository appointmentRepository,
            DoctorRepository doctorRepository,
            PatientRepository patientRepository) {

        this.appointmentRepository = appointmentRepository;
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
    }

    /* ================= CREATE ================= */
    public AppointmentResponseDTO createAppointment(AppointmentRequestDTO dto) {

        Doctor doctor = doctorRepository.findById(dto.getDoctorId())
                .orElseThrow(() -> new DoctorEntityNotFoundException(dto.getDoctorId()));

        Patient patient = patientRepository.findById(dto.getPatientId())
                .orElseThrow(() -> new PatientEntityNotFoundException(dto.getPatientId()));

        // Prevent double booking
        boolean alreadyBooked = appointmentRepository
                .existsByDoctorIdAndAppointmentDateAndTimeSlot(
                        doctor.getId(),
                        dto.getAppointmentDate(),
                        dto.getTimeSlot()
                );

        if (alreadyBooked) {
            throw new IllegalStateException(
                "Doctor is already booked for this time slot"
            );
        }

        Appointment appointment = new Appointment();
        appointment.setDoctor(doctor);
        appointment.setPatient(patient);
        appointment.setAppointmentDate(dto.getAppointmentDate());
        appointment.setTimeSlot(dto.getTimeSlot());
        appointment.setReasonForVisit(dto.getReasonForVisit());
        appointment.setStatus(dto.getStatus()); // SCHEDULED / CANCELLED / COMPLETED

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

        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new AppointmentNotFoundException(id));

        Doctor doctor = doctorRepository.findById(dto.getDoctorId())
                .orElseThrow(() -> new DoctorEntityNotFoundException(dto.getDoctorId()));

        Patient patient = patientRepository.findById(dto.getPatientId())
                .orElseThrow(() -> new PatientEntityNotFoundException(dto.getPatientId()));

        appointment.setDoctor(doctor);
        appointment.setPatient(patient);
        appointment.setAppointmentDate(dto.getAppointmentDate());
        appointment.setTimeSlot(dto.getTimeSlot());
        appointment.setReasonForVisit(dto.getReasonForVisit());
        appointment.setStatus(dto.getStatus());

        return toResponseDTO(appointmentRepository.save(appointment));
    }

    /* ================= DELETE ================= */
    public void deleteAppointment(Long id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new AppointmentNotFoundException(id));

        appointmentRepository.delete(appointment);
    }

    /* ================= DTO MAPPER ================= */
    private AppointmentResponseDTO toResponseDTO(Appointment appointment) {
        return new AppointmentResponseDTO(
                appointment.getId(),
                appointment.getAppointmentDate(),
                appointment.getTimeSlot(),
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
