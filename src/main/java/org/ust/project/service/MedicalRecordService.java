package org.ust.project.service;  // Corrected package name

import java.util.List;
import java.util.stream.Collectors;

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

    private final MedicalRecordRepository medicalRecordRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;

    public MedicalRecordService(MedicalRecordRepository medicalRecordRepository,
                                PatientRepository patientRepository,
                                DoctorRepository doctorRepository) {
        this.medicalRecordRepository = medicalRecordRepository;
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
    }

    /* CREATE */
    public MedicalRecordResponseDTO createMedicalRecord(MedicalRecordRequestDTO dto) {

        // Fetch the patient and doctor entities
        Patient patient = patientRepository.findById(dto.getPatientId())
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        Doctor doctor = doctorRepository.findById(dto.getDoctorId())
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        // Create the MedicalRecord entity
        MedicalRecord medicalRecord = new MedicalRecord();
        medicalRecord.setRecordDate(dto.getRecordDate());
        medicalRecord.setDiagnosis(dto.getDiagnosis());
        medicalRecord.setTreatmentPlan(dto.getTreatmentPlan());
        medicalRecord.setSymptoms(dto.getSymptoms());
        medicalRecord.setPatient(patient);
        medicalRecord.setDoctor(doctor);

        // Save the record
        medicalRecord = medicalRecordRepository.save(medicalRecord);

        // Return the response DTO
        return mapToResponse(medicalRecord);
    }

    /* GET ALL */
    public List<MedicalRecordResponseDTO> getAllMedicalRecords() {
        return medicalRecordRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /* GET BY ID */
    public MedicalRecordResponseDTO getMedicalRecordById(Long id) {
        MedicalRecord medicalRecord = medicalRecordRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Medical record not found"));
        return mapToResponse(medicalRecord);
    }

    /* UPDATE */
    public MedicalRecordResponseDTO updateMedicalRecord(Long id, MedicalRecordRequestDTO dto) {

        MedicalRecord existingRecord = medicalRecordRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Medical record not found"));

        // Update with new data
        Patient patient = patientRepository.findById(dto.getPatientId())
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        Doctor doctor = doctorRepository.findById(dto.getDoctorId())
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        existingRecord.setRecordDate(dto.getRecordDate());
        existingRecord.setDiagnosis(dto.getDiagnosis());
        existingRecord.setTreatmentPlan(dto.getTreatmentPlan());
        existingRecord.setSymptoms(dto.getSymptoms());
        existingRecord.setPatient(patient);
        existingRecord.setDoctor(doctor);

        // Save the updated record
        existingRecord = medicalRecordRepository.save(existingRecord);

        return mapToResponse(existingRecord);
    }

    /* DELETE */
    public void deleteMedicalRecord(Long id) {
        medicalRecordRepository.deleteById(id);
    }

    /* ===================== MAPPER ===================== */
    // Convert MedicalRecord to MedicalRecordResponseDTO
    private MedicalRecordResponseDTO mapToResponse(MedicalRecord record) {

        Doctor doctor = record.getDoctor();
        Patient patient = record.getPatient();

        DoctorResponseDTO doctorDTO = new DoctorResponseDTO(
                doctor.getId(),
                doctor.getFirstName(),
                doctor.getLastName(),
                doctor.getSpecialization(),
                doctor.getAvailabilitySchedule()
        );

        PatientResponseDTO patientDTO = new PatientResponseDTO(
                patient.getId(),
                patient.getFirstName(),
                patient.getLastName(),
                patient.getDateOfBirth(),
                patient.getGender(),
                patient.getPhoneNumber(),
                patient.getEmail(),
                patient.getBloodGroup()
        );

        return new MedicalRecordResponseDTO(
                record.getId(),
                record.getRecordDate(),
                record.getDiagnosis(),
                record.getTreatmentPlan(),
                record.getSymptoms(),
                doctorDTO,
                patientDTO
        );
    }
}
