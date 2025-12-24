package org.ust.project.controller;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.ust.project.dto.ConsultationRequestDTO;
import org.ust.project.dto.ConsultationResponseDTO;
import org.ust.project.service.ConsultationService;

@RestController
@RequestMapping("/consultations")
public class ConsultationController {

    @Autowired
    private ConsultationService consultationService;

    /* ================= CREATE ================= */
    @PostMapping
    public ResponseEntity<ConsultationResponseDTO> createConsultation(
            @Valid @RequestBody ConsultationRequestDTO requestDTO) {

        ConsultationResponseDTO response =
                consultationService.createConsultation(requestDTO);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    /* ================= GET BY ID ================= */
    @GetMapping("/{id}")
    public ResponseEntity<ConsultationResponseDTO> getConsultationById(
            @PathVariable Long id) {

        ConsultationResponseDTO response =
                consultationService.getConsultationById(id);

        return ResponseEntity.ok(response);
    }

    /* ================= GET ALL ================= */
    @GetMapping
    public ResponseEntity<List<ConsultationResponseDTO>> getAllConsultations() {

        List<ConsultationResponseDTO> responses =
                consultationService.getAllConsultations();

        return ResponseEntity.ok(responses);
    }

    /* ================= UPDATE ================= */
    @PutMapping("/{id}")
    public ResponseEntity<ConsultationResponseDTO> updateConsultation(
            @PathVariable Long id,
            @Valid @RequestBody ConsultationRequestDTO requestDTO) {

        ConsultationResponseDTO response =
                consultationService.updateConsultation(id, requestDTO);

        return ResponseEntity.ok(response);
    }

    /* ================= DELETE ================= */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteConsultation(@PathVariable Long id) {

        consultationService.deleteConsultation(id);

        return ResponseEntity.noContent().build();
    }
}
