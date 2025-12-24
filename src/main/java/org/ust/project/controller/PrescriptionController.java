package org.ust.project.controller;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.ust.project.dto.PrescriptionRequestDTO;
import org.ust.project.dto.PrescriptionResponseDTO;
import org.ust.project.service.PrescriptionService;

@RestController
@RequestMapping("/prescriptions")
@CrossOrigin
public class PrescriptionController {

    private final PrescriptionService prescriptionService;

    public PrescriptionController(PrescriptionService prescriptionService) {
        this.prescriptionService = prescriptionService;
    }

    /* ================= CREATE ================= */
    @PostMapping
    public ResponseEntity<PrescriptionResponseDTO> createPrescription(
            @Valid @RequestBody PrescriptionRequestDTO dto) {

        PrescriptionResponseDTO response =
                prescriptionService.createPrescription(dto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    /* ================= GET BY ID ================= */
    @GetMapping("/{id}")
    public ResponseEntity<PrescriptionResponseDTO> getPrescriptionById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                prescriptionService.getPrescriptionById(id)
        );
    }

    /* ================= GET ALL ================= */
    @GetMapping
    public ResponseEntity<List<PrescriptionResponseDTO>> getAllPrescriptions() {

        return ResponseEntity.ok(
                prescriptionService.getAllPrescriptions()
        );
    }

    /* ================= UPDATE ================= */
    @PutMapping("/{id}")
    public ResponseEntity<PrescriptionResponseDTO> updatePrescription(
            @PathVariable Long id,
            @Valid @RequestBody PrescriptionRequestDTO dto) {

        return ResponseEntity.ok(
                prescriptionService.updatePrescription(id, dto)
        );
    }

    /* ================= DELETE ================= */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePrescription(
            @PathVariable Long id) {

        prescriptionService.deletePrescription(id);
        return ResponseEntity.noContent().build();
    }
}
