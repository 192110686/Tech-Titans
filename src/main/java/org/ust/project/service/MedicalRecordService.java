package org.ust.project.service;  // Corrected package name

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ust.project.dto.DoctorResponseDTO;
import org.ust.project.dto.MedicalRecordRequestDTO;
import org.ust.project.dto.MedicalRecordResponseDTO;
import org.ust.project.dto.PatientResponseDTO;
import org.ust.project.model.Doctor;
import org.ust.project.model.MedicalRecord;
import org.ust.project.model.Patient;
import org.ust.project.repo.DoctorRepository;
import org.ust.project.repo.MedicalRecordRepository;
import org.ust.project.repo.PatientRepository;

@Service
public class MedicalRecordService {

    @Autowired
    private MedicalRecordRepository medicalRecordRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private PatientRepository patientRepository;

    // Create a new medical record
    public MedicalRecordResponseDTO createMedicalRecord(MedicalRecordRequestDTO medicalRecordRequestDTO) {
        MedicalRecord medicalRecord = new MedicalRecord();

        // Fetch doctor and patient using their IDs
        Optional<Doctor> doctorOptional = doctorRepository.findById(medicalRecordRequestDTO.getDoctorId());
        Optional<Patient> patientOptional = patientRepository.findById(medicalRecordRequestDTO.getPatientId());

        if (doctorOptional.isPresent() && patientOptional.isPresent()) {
            Doctor doctor = doctorOptional.get();
            Patient patient = patientOptional.get();

            // Setting the fields from the DTO to the entity
            medicalRecord.setRecordDate(medicalRecordRequestDTO.getRecordDate());
            medicalRecord.setDiagnosis(medicalRecordRequestDTO.getDiagnosis());
            medicalRecord.setTreatmentPlan(medicalRecordRequestDTO.getTreatmentPlan());
            medicalRecord.setSymptoms(medicalRecordRequestDTO.getSymptoms());
            medicalRecord.setDoctor(doctor);
            medicalRecord.setPatient(patient);

            // Save the medical record
            medicalRecord = medicalRecordRepository.save(medicalRecord);

            // Convert doctor and patient to their response DTOs
            DoctorResponseDTO doctorResponseDTO = new DoctorResponseDTO(
                doctor.getId(), doctor.getFirstName(), doctor.getLastName(), doctor.getSpecialization(), doctor.getAvailabilitySchedule()
            );
            PatientResponseDTO patientResponseDTO = new PatientResponseDTO(
                patient.getId(), patient.getFirstName(), patient.getLastName(), patient.getDateOfBirth(), patient.getGender(),
                patient.getPhoneNumber(), patient.getEmail(), patient.getBloodGroup()
            );

            // Return the response DTO
            return new MedicalRecordResponseDTO(
                medicalRecord.getId(),
                medicalRecord.getRecordDate(),
                medicalRecord.getDiagnosis(),
                medicalRecord.getTreatmentPlan(),
                medicalRecord.getSymptoms(),
                doctorResponseDTO,
                patientResponseDTO
            );
        }
        return null; // Handle doctor or patient not found scenario (you can throw exceptions here)
    }

    // Get medical record by ID
    public MedicalRecordResponseDTO getMedicalRecordById(Long id) {
        Optional<MedicalRecord> medicalRecordOptional = medicalRecordRepository.findById(id);
        if (medicalRecordOptional.isPresent()) {
            MedicalRecord medicalRecord = medicalRecordOptional.get();

            // Convert doctor and patient to their response DTOs
            DoctorResponseDTO doctorResponseDTO = new DoctorResponseDTO(
                medicalRecord.getDoctor().getId(), medicalRecord.getDoctor().getFirstName(),
                medicalRecord.getDoctor().getLastName(), medicalRecord.getDoctor().getSpecialization(), medicalRecord.getDoctor().getAvailabilitySchedule()
            );
            PatientResponseDTO patientResponseDTO = new PatientResponseDTO(
                medicalRecord.getPatient().getId(), medicalRecord.getPatient().getFirstName(),
                medicalRecord.getPatient().getLastName(), medicalRecord.getPatient().getDateOfBirth(),
                medicalRecord.getPatient().getGender(), medicalRecord.getPatient().getPhoneNumber(),
                medicalRecord.getPatient().getEmail(), medicalRecord.getPatient().getBloodGroup()
            );

            // Return response DTO
            return new MedicalRecordResponseDTO(
                medicalRecord.getId(),
                medicalRecord.getRecordDate(),
                medicalRecord.getDiagnosis(),
                medicalRecord.getTreatmentPlan(),
                medicalRecord.getSymptoms(),
                doctorResponseDTO,
                patientResponseDTO
            );
        }
        return null;
    }

    // Get all medical records
    public List<MedicalRecordResponseDTO> getAllMedicalRecords() {
        List<MedicalRecord> medicalRecords = medicalRecordRepository.findAll();
        return medicalRecords.stream()
            .map(medicalRecord -> {
                // Convert doctor and patient to their response DTOs
                DoctorResponseDTO doctorResponseDTO = new DoctorResponseDTO(
                    medicalRecord.getDoctor().getId(), medicalRecord.getDoctor().getFirstName(),
                    medicalRecord.getDoctor().getLastName(), medicalRecord.getDoctor().getSpecialization(), medicalRecord.getDoctor().getAvailabilitySchedule()
                );
                PatientResponseDTO patientResponseDTO = new PatientResponseDTO(
                    medicalRecord.getPatient().getId(), medicalRecord.getPatient().getFirstName(),
                    medicalRecord.getPatient().getLastName(), medicalRecord.getPatient().getDateOfBirth(),
                    medicalRecord.getPatient().getGender(), medicalRecord.getPatient().getPhoneNumber(),
                    medicalRecord.getPatient().getEmail(), medicalRecord.getPatient().getBloodGroup()
                );

                // Return the response DTO
                return new MedicalRecordResponseDTO(
                    medicalRecord.getId(),
                    medicalRecord.getRecordDate(),
                    medicalRecord.getDiagnosis(),
                    medicalRecord.getTreatmentPlan(),
                    medicalRecord.getSymptoms(),
                    doctorResponseDTO,
                    patientResponseDTO
                );
            })
            .collect(Collectors.toList());
    }

    // Update a medical record
    public MedicalRecordResponseDTO updateMedicalRecord(Long id, MedicalRecordRequestDTO medicalRecordRequestDTO) {
        Optional<MedicalRecord> medicalRecordOptional = medicalRecordRepository.findById(id);
        if (medicalRecordOptional.isPresent()) {
            MedicalRecord medicalRecord = medicalRecordOptional.get();

            // Fetch doctor and patient using IDs
            Optional<Doctor> doctorOptional = doctorRepository.findById(medicalRecordRequestDTO.getDoctorId());
            Optional<Patient> patientOptional = patientRepository.findById(medicalRecordRequestDTO.getPatientId());

            if (doctorOptional.isPresent() && patientOptional.isPresent()) {
                medicalRecord.setRecordDate(medicalRecordRequestDTO.getRecordDate());
                medicalRecord.setDiagnosis(medicalRecordRequestDTO.getDiagnosis());
                medicalRecord.setTreatmentPlan(medicalRecordRequestDTO.getTreatmentPlan());
                medicalRecord.setSymptoms(medicalRecordRequestDTO.getSymptoms());
                medicalRecord.setDoctor(doctorOptional.get());
                medicalRecord.setPatient(patientOptional.get());

                medicalRecord = medicalRecordRepository.save(medicalRecord);

                // Convert doctor and patient to response DTOs
                DoctorResponseDTO doctorResponseDTO = new DoctorResponseDTO(
                    medicalRecord.getDoctor().getId(), medicalRecord.getDoctor().getFirstName(),
                    medicalRecord.getDoctor().getLastName(), medicalRecord.getDoctor().getSpecialization(), medicalRecord.getDoctor().getAvailabilitySchedule()
                );
                PatientResponseDTO patientResponseDTO = new PatientResponseDTO(
                    medicalRecord.getPatient().getId(), medicalRecord.getPatient().getFirstName(),
                    medicalRecord.getPatient().getLastName(), medicalRecord.getPatient().getDateOfBirth(),
                    medicalRecord.getPatient().getGender(), medicalRecord.getPatient().getPhoneNumber(),
                    medicalRecord.getPatient().getEmail(), medicalRecord.getPatient().getBloodGroup()
                );

                // Return response DTO
                return new MedicalRecordResponseDTO(
                    medicalRecord.getId(),
                    medicalRecord.getRecordDate(),
                    medicalRecord.getDiagnosis(),
                    medicalRecord.getTreatmentPlan(),
                    medicalRecord.getSymptoms(),
                    doctorResponseDTO,
                    patientResponseDTO
                );
            }
        }
        return null;
    }

    // Delete a medical record
    public boolean deleteMedicalRecord(Long id) {
        if (medicalRecordRepository.existsById(id)) {
            medicalRecordRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
