package org.ust.project.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.ust.project.dto.DoctorRequestDTO;
import org.ust.project.dto.DoctorResponseDTO;
import org.ust.project.exception.DoctorEntityNotFoundException;
import org.ust.project.model.Appointment;
import org.ust.project.model.Doctor;
import org.ust.project.repo.AppointmentRepository;
import org.ust.project.repo.DoctorRepository;

@Service
@Transactional
public class DoctorService {

    private final DoctorRepository doctorRepository;
    private final AppointmentRepository appointmentRepository;

    public DoctorService(
            DoctorRepository doctorRepository,
            AppointmentRepository appointmentRepository) {
        this.doctorRepository = doctorRepository;
        this.appointmentRepository = appointmentRepository;
    }

    /* ================= CREATE ================= */
    public DoctorResponseDTO createDoctor(DoctorRequestDTO dto) {
        Doctor doctor = new Doctor();
        doctor.setFirstName(dto.getFirstName());
        doctor.setLastName(dto.getLastName());
        doctor.setSpecialization(dto.getSpecialization());
        doctor.setContactNumber(dto.getContactNumber());
        doctor.setEmail(dto.getEmail());
        doctor.setLicenseNumber(dto.getLicenseNumber());
        doctor.setAvailableSchedule(dto.getAvailableSchedule());

        return toResponseDTO(doctorRepository.save(doctor));
    }

    /* ================= GET BY ID ================= */
    public DoctorResponseDTO getDoctorById(Long doctorId) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new DoctorEntityNotFoundException(doctorId));
        return toResponseDTO(doctor);
    }

    /* ================= GET ALL ================= */
    public List<DoctorResponseDTO> getAllDoctors() {
        return doctorRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    /* ================= UPDATE ================= */
    public DoctorResponseDTO updateDoctor(Long id, DoctorRequestDTO dto) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new DoctorEntityNotFoundException(id));

        doctor.setFirstName(dto.getFirstName());
        doctor.setLastName(dto.getLastName());
        doctor.setSpecialization(dto.getSpecialization());
        doctor.setContactNumber(dto.getContactNumber());
        doctor.setEmail(dto.getEmail());
        doctor.setLicenseNumber(dto.getLicenseNumber());
        doctor.setAvailableSchedule(dto.getAvailableSchedule());

        return toResponseDTO(doctorRepository.save(doctor));
    }

    /* ================= DELETE ================= */
    public void deleteDoctor(Long id) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new DoctorEntityNotFoundException(id));

        if (doctor.getAppointments() != null && !doctor.getAppointments().isEmpty()) {
            throw new IllegalStateException("Cannot delete doctor with existing appointments");
        }

        doctorRepository.delete(doctor);
    }

    public List<LocalDateTime> getAvailableSlots(Long doctorId, LocalDateTime startTime, LocalDateTime endTime) {
        // Get the doctor by ID
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        // Get existing appointments for the doctor during the specified time range
        List<Appointment> existingAppointments = appointmentRepository
                .findByDoctorAndAppointmentDateTimeBetween(doctor, startTime, endTime);

        List<LocalDateTime> availableSlots = new ArrayList<>();

        // Check availability slot by slot
        LocalDateTime slot = startTime;
        while (slot.isBefore(endTime)) {
            final LocalDateTime currentSlot = slot;
            boolean isAvailable = existingAppointments.stream()
                    .noneMatch(appointment -> appointment.getAppointmentDateTime().equals(currentSlot));

            if (isAvailable) {
                availableSlots.add(slot);
            }

            slot = slot.plusMinutes(30);
        }

        return availableSlots;
    }

    private DoctorResponseDTO toResponseDTO(Doctor doctor) {
        return new DoctorResponseDTO(
                doctor.getId(),
                doctor.getFirstName(),
                doctor.getLastName(),
                doctor.getSpecialization(),
                doctor.getAvailableSchedule()
        );
    }
}
