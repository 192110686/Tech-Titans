package org.ust.project.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ust.project.model.Patient;
import org.ust.project.repo.PatientRepository;

import java.util.List;
import java.util.Optional;

@Service
public class PatientService {

    @Autowired
    private PatientRepository patientRepository;

    // 1. Get All Patients
    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    // 2. Get Single Patient by ID
    public Optional<Patient> getPatientById(Long id) {
        return patientRepository.findById(id);
    }

    // 3. Create or Update Patient
    public Patient savePatient(Patient patient) {
        return patientRepository.save(patient);
    }

    // 4. Delete Patient
    public void deletePatient(Long id) {
        patientRepository.deleteById(id);
    }
}