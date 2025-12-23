package org.ust.project.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ust.project.dto.UserRequestDTO;
import org.ust.project.dto.UserResponseDTO;
import org.ust.project.model.Doctor;
import org.ust.project.model.Patient;
import org.ust.project.model.User;
import org.ust.project.repo.DoctorRepository;
import org.ust.project.repo.PatientRepository;
import org.ust.project.repo.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    // CREATE USER
    public UserResponseDTO createUser(UserRequestDTO dto) {

        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(dto.getPassword()); // no encryption (you should add encryption in real projects)
        user.setRole(dto.getRole());
        user.setRegistrationDate(LocalDate.now());

        // Check if the user is a patient or a doctor and set the appropriate relationship
        if ("PATIENT".equalsIgnoreCase(dto.getRole())) {
            // Fetch the patient by patientId from the request
            Patient patient = patientRepository.findById(dto.getPatientId())
                    .orElseThrow(() -> new RuntimeException("Patient not found"));

            // Set the patient relationship in User
            user.setPatient(patient);
        } else if ("DOCTOR".equalsIgnoreCase(dto.getRole())) {
            // Fetch the doctor by doctorId from the request
            Doctor doctor = doctorRepository.findById(dto.getDoctorId())
                    .orElseThrow(() -> new RuntimeException("Doctor not found"));

            // Set the doctor relationship in User
            user.setDoctor(doctor);
        }

        // Save the user and return the response DTO
        User saved = userRepository.save(user);

        return convert(saved);
    }

    // GET USER BY ID
    public UserResponseDTO getUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return convert(user);
    }

    // GET ALL USERS
    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::convert)
                .collect(Collectors.toList());
    }

    // DELETE USER
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found");
        }
        userRepository.deleteById(id);
    }

    // MAP TO DTO
    private UserResponseDTO convert(User user) {
        return new UserResponseDTO(
                user.getId(),
                user.getUsername(),
                user.getRole()
        );
    }
}
