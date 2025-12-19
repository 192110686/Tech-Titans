package org.ust.project.controller;

import java.util.List;

import org.ust.project.dto.MedicalRecordRequestDTO;
import org.ust.project.dto.MedicalRecordResponseDTO;
import org.ust.project.service.MedicalRecordService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/medical-records")
public class MedicalRecordController {

    private final MedicalRecordService medicalRecordService;

    public MedicalRecordController(MedicalRecordService medicalRecordService) {
        this.medicalRecordService = medicalRecordService;
    }

    /* ================= CREATE ================= */
    @PostMapping
    public ResponseEntity<MedicalRecordResponseDTO> createMedicalRecord(
            @RequestBody MedicalRecordRequestDTO requestDTO) {

        MedicalRecordResponseDTO response =
                medicalRecordService.createMedicalRecord(requestDTO);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /* ================= GET ALL ================= */
    @GetMapping
    public ResponseEntity<List<MedicalRecordResponseDTO>> getAllMedicalRecords() {

        List<MedicalRecordResponseDTO> records =
                medicalRecordService.getAllMedicalRecords();

        return ResponseEntity.ok(records);
    }

    /* ================= GET BY ID ================= */
    @GetMapping("/{id}")
    public ResponseEntity<MedicalRecordResponseDTO> getMedicalRecordById(
            @PathVariable Long id) {

        MedicalRecordResponseDTO record =
                medicalRecordService.getMedicalRecordById(id);

        return ResponseEntity.ok(record);
    }

    /* ================= UPDATE ================= */
    @PutMapping("/{id}")
    public ResponseEntity<MedicalRecordResponseDTO> updateMedicalRecord(
            @PathVariable Long id,
            @RequestBody MedicalRecordRequestDTO requestDTO) {

        MedicalRecordResponseDTO updatedRecord =
                medicalRecordService.updateMedicalRecord(id, requestDTO);

        return ResponseEntity.ok(updatedRecord);
    }

    /* ================= DELETE ================= */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMedicalRecord(@PathVariable Long id) {

        medicalRecordService.deleteMedicalRecord(id);
        return ResponseEntity.noContent().build();
    }
}
