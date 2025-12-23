package org.ust.project.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.ust.project.dto.DoctorRequestDTO;
import org.ust.project.dto.DoctorResponseDTO;
import org.ust.project.exception.DoctorEntityNotFoundException;
import org.ust.project.model.Doctor;
import org.ust.project.repo.DoctorRepository;

@Service
@Transactional
public class DoctorService {

    private final DoctorRepository doctorRepository;

    public DoctorService(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
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
    public DoctorResponseDTO getDoctorById(Long id) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new DoctorEntityNotFoundException(id));

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
        doctor.setAvailabilitySchedule(dto.getAvailabilitySchedule());

        return toResponseDTO(doctorRepository.save(doctor));
    }

    /* ================= DELETE ================= */
    public void deleteDoctor(Long id) {

        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new DoctorEntityNotFoundException(id));

        // Safety check: doctor should not have active appointments
        if (doctor.getAppointments() != null && !doctor.getAppointments().isEmpty()) {
            throw new IllegalStateException(
                "Cannot delete doctor with existing appointments"
            );
        }

        doctorRepository.delete(doctor);
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
