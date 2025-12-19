package org.ust.project.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.ust.project.dto.PrescriptionRequestDTO;
import org.ust.project.dto.PrescriptionResponseDTO;
import org.ust.project.service.PrescriptionService;

@RestController
@RequestMapping("/prescriptions")
@CrossOrigin
public class PrescriptionController {

    @Autowired
    private PrescriptionService prescriptionService;

    // CREATE PRESCRIPTION
    @PostMapping
    public ResponseEntity<PrescriptionResponseDTO> create(@RequestBody PrescriptionRequestDTO dto) {
        PrescriptionResponseDTO response = prescriptionService.createPrescription(dto);
        return ResponseEntity.ok(response);
    }

    // GET PRESCRIPTION BY ID
    @GetMapping("/{id}")
    public ResponseEntity<PrescriptionResponseDTO> getById(@PathVariable Long id) {
        PrescriptionResponseDTO response = prescriptionService.getPrescriptionById(id);
        return ResponseEntity.ok(response);
    }

    // GET ALL PRESCRIPTIONS
    @GetMapping
    public ResponseEntity<List<PrescriptionResponseDTO>> getAll() {
        List<PrescriptionResponseDTO> response = prescriptionService.getAllPrescriptions();
        return ResponseEntity.ok(response);
    }

    // DELETE PRESCRIPTION BY ID
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteById(@PathVariable Long id) {
        prescriptionService.deletePrescription(id);
        return ResponseEntity.ok("Prescription deleted successfully");
    }
}
