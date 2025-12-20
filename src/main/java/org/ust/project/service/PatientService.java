package org.ust.project.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.ust.project.dto.PatientRequestDTO;
import org.ust.project.dto.PatientResponseDTO;
import org.ust.project.exception.PatientEntityNotFoundException;
import org.ust.project.model.Patient;
import org.ust.project.repo.PatientRepository;


@Service
public class PatientService {

    private final PatientRepository patientRepository;

    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    /* ================= CREATE ================= */
    public PatientResponseDTO createPatient(PatientRequestDTO dto) {

        Patient patient = new Patient();
        patient.setFirstName(dto.getFirstName());
        patient.setLastName(dto.getLastName());
        patient.setDateOfBirth(dto.getDateOfBirth());
        patient.setGender(dto.getGender());
        patient.setPhoneNumber(dto.getPhoneNumber());
        patient.setEmail(dto.getEmail());
        patient.setBloodGroup(dto.getBloodGroup());
        patient.setAddress(dto.getAddress());
        patient.setRegistrationDate(LocalDate.now());

        Patient savedPatient = patientRepository.save(patient);

        return toResponseDTO(savedPatient);
    }

    /* ================= GET ALL ================= */
    public List<PatientResponseDTO> getAllPatients() {
        return patientRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    /* ================= GET BY ID ================= */
    public PatientResponseDTO getPatientById(Long id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new PatientEntityNotFoundException(id));

        return toResponseDTO(patient);
    }

    /* ================= UPDATE ================= */
    public PatientResponseDTO updatePatient(Long id, PatientRequestDTO dto) {

        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new PatientEntityNotFoundException(id));

        patient.setFirstName(dto.getFirstName());
        patient.setLastName(dto.getLastName());
        patient.setDateOfBirth(dto.getDateOfBirth());
        patient.setGender(dto.getGender());
        patient.setPhoneNumber(dto.getPhoneNumber());
        patient.setEmail(dto.getEmail());
        patient.setBloodGroup(dto.getBloodGroup());
        patient.setAddress(dto.getAddress());

        Patient updatedPatient = patientRepository.save(patient);

        return toResponseDTO(updatedPatient);
    }

    /* ================= DELETE ================= */
    public void deletePatient(Long id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new PatientEntityNotFoundException(id));

        patientRepository.delete(patient);
    }

    /* ================= DTO CONVERSION ================= */
    private PatientResponseDTO toResponseDTO(Patient patient) {
        return new PatientResponseDTO(
                patient.getId(),
                patient.getFirstName(),
                patient.getLastName(),
                patient.getDateOfBirth(),
                patient.getGender(),
                patient.getPhoneNumber(),
                patient.getEmail(),
                patient.getBloodGroup()
        );
    }
}
