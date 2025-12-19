package org.ust.project.controller;

import java.util.List;

import org.ust.project.dto.PatientRequestDTO;
import org.ust.project.dto.PatientResponseDTO;
import org.ust.project.service.PatientService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/patients")
public class PatientController {

    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    /* ================= CREATE ================= */
    @PostMapping
    public ResponseEntity<PatientResponseDTO> createPatient(
            @RequestBody PatientRequestDTO requestDTO) {

        PatientResponseDTO response =
                patientService.createPatient(requestDTO);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /* ================= GET ALL ================= */
    @GetMapping
    public ResponseEntity<List<PatientResponseDTO>> getAllPatients() {

        return ResponseEntity.ok(patientService.getAllPatients());
    }

    /* ================= GET BY ID ================= */
    @GetMapping("/{id}")
    public ResponseEntity<PatientResponseDTO> getPatientById(
            @PathVariable Long id) {

        return ResponseEntity.ok(patientService.getPatientById(id));
    }

    /* ================= UPDATE ================= */
    @PutMapping("/{id}")
    public ResponseEntity<PatientResponseDTO> updatePatient(
            @PathVariable Long id,
            @RequestBody PatientRequestDTO requestDTO) {

        return ResponseEntity.ok(
                patientService.updatePatient(id, requestDTO)
        );
    }

    /* ================= DELETE ================= */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePatient(@PathVariable Long id) {

        patientService.deletePatient(id);
        return ResponseEntity.noContent().build();
    }
}
