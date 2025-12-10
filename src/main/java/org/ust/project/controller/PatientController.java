package org.ust.project.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.ust.project.model.Patient;
import org.ust.project.service.PatientService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/patients")
@CrossOrigin("*") // Allows your frontend (React/Angular) to connect easily
public class PatientController {

    @Autowired
    private PatientService patientService;

    // GET: http://localhost:8080/api/patients
    @GetMapping
    public List<Patient> getAllPatients() {
        return patientService.getAllPatients();
    }

    // GET: http://localhost:8080/api/patients/1
    @GetMapping("/{id}")
    public Patient getPatientById(@PathVariable Long id) {
        return patientService.getPatientById(id).orElse(null);
    }

    // POST: http://localhost:8080/api/patients
    @PostMapping
    public Patient createPatient(@RequestBody Patient patient) {
        return patientService.savePatient(patient);
    }

    // PUT: http://localhost:8080/api/patients/1
    @PutMapping("/{id}")
    public Patient updatePatient(@PathVariable Long id, @RequestBody Patient patientDetails) {
        // Simple update logic
        Patient patient = patientService.getPatientById(id).orElse(null);
        if (patient != null) {
            patient.setFirstName(patientDetails.getFirstName());
            patient.setLastName(patientDetails.getLastName());
            patient.setPhoneNumber(patientDetails.getPhoneNumber());
            // ... set other fields ...
            return patientService.savePatient(patient);
        }
        return null;
    }

    // DELETE: http://localhost:8080/api/patients/1
    @DeleteMapping("/{id}")
    public void deletePatient(@PathVariable Long id) {
        patientService.deletePatient(id);
    }
}