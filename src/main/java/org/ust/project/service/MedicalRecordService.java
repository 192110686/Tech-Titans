package org.ust.project.service;

import org.ust.project.model.Doctor;
import org.ust.project.model.MedicalRecord;
import org.ust.project.model.Patient;
import org.ust.project.repo.DoctorRepository;
import org.ust.project.repo.MedicalRecordRepository;
import org.ust.project.repo.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MedicalRecordService {

    @Autowired
    private MedicalRecordRepository medicalRecordRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    public List<MedicalRecord> getAllMedicalRecords() {
        return medicalRecordRepository.findAll();
    }

    public Optional<MedicalRecord> getMedicalRecordById(Long id) {
        return medicalRecordRepository.findById(id);
    }

    // Logic to link Patient and Doctor to the Record
    public MedicalRecord createMedicalRecord(Long patientId, Long doctorId, MedicalRecord record) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found with ID: " + patientId));

        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new RuntimeException("Doctor not found with ID: " + doctorId));

        record.setPatient(patient);
        record.setDoctor(doctor);
        record.setRecordDate(java.time.LocalDate.now()); // Default to today

        return medicalRecordRepository.save(record);
    }

    public void deleteMedicalRecord(Long id) {
        medicalRecordRepository.deleteById(id);
    }
}