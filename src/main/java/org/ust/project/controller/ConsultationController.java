package org.ust.project.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.ust.project.dto.ConsultationRequestDTO;
import org.ust.project.dto.ConsultationResponseDTO;
import org.ust.project.service.ConsultationService;

@RestController
@RequestMapping("/consultations")
public class ConsultationController {

	@Autowired
    private ConsultationService consultationService;

    /* ================= CREATE CONSULTATION ================= */
    @PostMapping
    public ResponseEntity<ConsultationResponseDTO> createConsultation(@RequestBody ConsultationRequestDTO consultationRequestDTO) {
        ConsultationResponseDTO consultationResponseDTO = consultationService.createConsultation(consultationRequestDTO);
        return ResponseEntity.ok(consultationResponseDTO);
    }

    /* ================= GET CONSULTATION BY ID ================= */
    @GetMapping("/{id}")
    public ResponseEntity<ConsultationResponseDTO> getConsultationById(@PathVariable Long id) {
        ConsultationResponseDTO consultationResponseDTO = consultationService.getConsultationById(id);
        return ResponseEntity.ok(consultationResponseDTO);
    }

    /* ================= GET ALL CONSULTATIONS ================= */
    @GetMapping
    public ResponseEntity<List<ConsultationResponseDTO>> getAllConsultations() {
        List<ConsultationResponseDTO> consultations = consultationService.getAllConsultations();
        return ResponseEntity.ok(consultations);
    }

    /* ================= COMPLETE CONSULTATION ================= */
    @PutMapping("/{id}/complete")
    public ResponseEntity<String> completeConsultation(@PathVariable Long id) {
        consultationService.completeConsultation(id);
        return ResponseEntity.ok("Consultation completed successfully!");
    }

    

   
}
