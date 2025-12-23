package org.ust.project.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.ust.project.dto.UserRequestDTO;
import org.ust.project.dto.UserResponseDTO;
import org.ust.project.model.Doctor;
import org.ust.project.model.Patient;
import org.ust.project.model.User;
import org.ust.project.repo.DoctorRepository;
import org.ust.project.repo.PatientRepository;
import org.ust.project.repo.UserRepository;

@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    /* ================= CREATE USER ================= */
    public UserResponseDTO createUser(UserRequestDTO dto) {

        if (dto.getRole() == null) {
            throw new IllegalArgumentException("Role is required");
        }

        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(dto.getPassword()); // TODO: encrypt using BCrypt
        user.setRole(dto.getRole());
        user.setRegistrationDate(LocalDate.now());

        if ("PATIENT".equalsIgnoreCase(dto.getRole())) {

            if (dto.getPatientId() == null) {
                throw new IllegalArgumentException("patientId is required for PATIENT role");
            }

            Patient patient = patientRepository.findById(dto.getPatientId())
                    .orElseThrow(() -> new RuntimeException("Patient not found"));

            // 🔥 Maintain BOTH sides
            user.setPatient(patient);
            patient.setUser(user);

        } else if ("DOCTOR".equalsIgnoreCase(dto.getRole())) {

            if (dto.getDoctorId() == null) {
                throw new IllegalArgumentException("doctorId is required for DOCTOR role");
            }

            Doctor doctor = doctorRepository.findById(dto.getDoctorId())
                    .orElseThrow(() -> new RuntimeException("Doctor not found"));

            // 🔥 Maintain BOTH sides
            user.setDoctor(doctor);
            doctor.setUser(user);

        } else {
            throw new IllegalArgumentException("Invalid role: " + dto.getRole());
        }

        User savedUser = userRepository.save(user);
        return toResponseDTO(savedUser);
    }

    /* ================= GET USER BY ID ================= */
    public UserResponseDTO getUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        return toResponseDTO(user);
    }

    /* ================= GET ALL USERS ================= */
    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    /* ================= DELETE USER ================= */
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        // Optional cleanup
        if (user.getPatient() != null) {
            user.getPatient().setUser(null);
        }
        if (user.getDoctor() != null) {
            user.getDoctor().setUser(null);
        }

        userRepository.delete(user);
    }

    /* ================= DTO MAPPER ================= */
    private UserResponseDTO toResponseDTO(User user) {
        return new UserResponseDTO(
                user.getId(),
                user.getUsername(),
                user.getRole()
        );
    }
}
