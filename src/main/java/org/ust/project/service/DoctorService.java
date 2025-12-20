package org.ust.project.service;

import org.ust.project.dto.DoctorRequestDTO;
import org.ust.project.dto.DoctorResponseDTO;
import org.ust.project.exception.DoctorEntityNotFoundException;
import org.ust.project.model.Doctor;
import org.ust.project.repo.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DoctorService {

    @Autowired
    private DoctorRepository doctorRepository;

    // Create a new doctor
    public DoctorResponseDTO createDoctor(DoctorRequestDTO doctorRequestDTO) {
        Doctor doctor = new Doctor();
        doctor.setFirstName(doctorRequestDTO.getFirstName());
        doctor.setLastName(doctorRequestDTO.getLastName());
        doctor.setSpecialization(doctorRequestDTO.getSpecialization());
        doctor.setContactNumber(doctorRequestDTO.getContactNumber());
        doctor.setEmail(doctorRequestDTO.getEmail());
        doctor.setLicenseNumber(doctorRequestDTO.getLicenseNumber());
        doctor.setAvailabilitySchedule(doctorRequestDTO.getAvailabilitySchedule());

        doctor = doctorRepository.save(doctor);

        return new DoctorResponseDTO(doctor.getId(), doctor.getFirstName(), doctor.getLastName(),
                doctor.getSpecialization(), doctor.getAvailabilitySchedule());
    }

    // Get doctor by ID
    public DoctorResponseDTO getDoctorById(Long id) {
        Optional<Doctor> doctorOptional = doctorRepository.findById(id);
        if (doctorOptional.isPresent()) {
            Doctor doctor = doctorOptional.get();
            return new DoctorResponseDTO(doctor.getId(), doctor.getFirstName(), doctor.getLastName(),
                    doctor.getSpecialization(), doctor.getAvailabilitySchedule());
        }
        throw new DoctorEntityNotFoundException(id); // Or throw an exception if you prefer
    }

    // Get all doctors
    public List<DoctorResponseDTO> getAllDoctors() {
        List<Doctor> doctors = doctorRepository.findAll();
        return doctors.stream()
                .map(doctor -> new DoctorResponseDTO(doctor.getId(), doctor.getFirstName(), doctor.getLastName(),
                        doctor.getSpecialization(), doctor.getAvailabilitySchedule()))
                .collect(Collectors.toList());
    }

    // Update doctor details
    public DoctorResponseDTO updateDoctor(Long id, DoctorRequestDTO doctorRequestDTO) {
        Optional<Doctor> doctorOptional = doctorRepository.findById(id);
        if (doctorOptional.isPresent()) {
            Doctor doctor = doctorOptional.get();
            doctor.setFirstName(doctorRequestDTO.getFirstName());
            doctor.setLastName(doctorRequestDTO.getLastName());
            doctor.setSpecialization(doctorRequestDTO.getSpecialization());
            doctor.setContactNumber(doctorRequestDTO.getContactNumber());
            doctor.setEmail(doctorRequestDTO.getEmail());
            doctor.setLicenseNumber(doctorRequestDTO.getLicenseNumber());
            doctor.setAvailabilitySchedule(doctorRequestDTO.getAvailabilitySchedule());

            doctor = doctorRepository.save(doctor);

            return new DoctorResponseDTO(doctor.getId(), doctor.getFirstName(), doctor.getLastName(),
                    doctor.getSpecialization(), doctor.getAvailabilitySchedule());
        }
        throw new DoctorEntityNotFoundException(id); // Or throw an exception if doctor not found
    }

    // Delete doctor
    public boolean deleteDoctor(Long id) {
        if (doctorRepository.existsById(id)) {
            doctorRepository.deleteById(id);
            return true;
        }
        throw new DoctorEntityNotFoundException(id); // Or throw an exception if doctor not found
    }
}
