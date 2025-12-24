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

    public DoctorService(DoctorRepository doctorRepository, AppointmentRepository appointmentRepository) {
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
        doctor.setAvailabilitySchedule(dto.getAvailabilitySchedule());

        return toResponseDTO(doctorRepository.save(doctor));
    }

    /* ================= GET BY ID ================= */
    public DoctorResponseDTO getDoctorById(Long doctorId) {
        // Fetch the doctor by ID from the repository
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new DoctorEntityNotFoundException(doctorId));

        // Convert Doctor entity to DoctorResponseDTO
        return new DoctorResponseDTO(
                doctor.getId(),
                doctor.getFirstName(),
                doctor.getLastName(),
                doctor.getSpecialization(),
                doctor.getAvailabilitySchedule()
        );
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
        doctor.setAvailabilitySchedule(dto.getAvailabilitySchedule());

        return toResponseDTO(doctorRepository.save(doctor));
    }

    /* ================= DELETE ================= */
    public void deleteDoctor(Long id) {

        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new DoctorEntityNotFoundException(id));

        // Safety check: doctor should not have active appointments
        if (doctor.getAppointments() != null && !doctor.getAppointments().isEmpty()) {
            throw new IllegalStateException("Cannot delete doctor with existing appointments");
        }

        doctorRepository.delete(doctor);
    }

    /* ================= Return Available Slots for Doctor ================= */
   public List<LocalDateTime> getAvailableSlots(Long doctorId, LocalDateTime startTime, LocalDateTime endTime) {
    // Get the doctor by ID
    Doctor doctor = doctorRepository.findById(doctorId)
            .orElseThrow(() -> new RuntimeException("Doctor not found"));

    // Get existing appointments for the doctor during the specified time range
    List<Appointment> existingAppointments = appointmentRepository
            .findByDoctorAndAppointmentDateTimeBetween(doctor, startTime, endTime);

    List<LocalDateTime> availableSlots = new ArrayList<>();

    // Assuming the doctor works from 9 AM to 5 PM, and each slot is 30 minutes
    LocalDateTime slot = startTime;
    
    // Check availability slot by slot, and add to the available slots list
    while (slot.isBefore(endTime)) {
        // Check if the slot is available (not already booked)
        final LocalDateTime currentSlot = slot;
        boolean isAvailable = existingAppointments.stream()
                .noneMatch(appointment -> appointment.getAppointmentDateTime().equals(currentSlot));
        
        if (isAvailable) {
            availableSlots.add(slot);
        }
        
        // Increment slot by 30 minutes
        slot = slot.plusMinutes(30);
    }

    return availableSlots;
}


    /* ================= DTO MAPPER ================= */
    private DoctorResponseDTO toResponseDTO(Doctor doctor) {
        return new DoctorResponseDTO(
                doctor.getId(),
                doctor.getFirstName(),
                doctor.getLastName(),
                doctor.getSpecialization(),
                doctor.getAvailabilitySchedule()
        );
    }
}
