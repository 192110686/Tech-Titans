package org.ust.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.ust.project.dto.ConsultationRequestDTO;
import org.ust.project.dto.ConsultationResponseDTO;
import org.ust.project.exception.ConsultationNotFoundException;
import org.ust.project.service.ConsultationService;

@RestController
@RequestMapping("consultations")
public class ConsultationController {

    @Autowired
    private ConsultationService consultationService;

    // Create Consultation
    @PostMapping
    public ResponseEntity<ConsultationResponseDTO> createConsultation(@RequestBody ConsultationRequestDTO consultationRequestDTO) {
        ConsultationResponseDTO response = consultationService.createConsultation(consultationRequestDTO);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // Update Consultation
    @PutMapping("/{id}")
    public ResponseEntity<ConsultationResponseDTO> updateConsultation(@PathVariable Long id, @RequestBody ConsultationRequestDTO consultationRequestDTO) {
        ConsultationResponseDTO response = consultationService.updateConsultation(id, consultationRequestDTO);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // Get Consultation by ID
    @GetMapping("/{id}")
    public ResponseEntity<ConsultationResponseDTO> getConsultationById(@PathVariable Long id) {
        ConsultationResponseDTO response = consultationService.getConsultationById(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // Delete Consultation
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteConsultation(@PathVariable Long id) {
        consultationService.deleteConsultation(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // Global exception handler for Consultation not found
    @ExceptionHandler(ConsultationNotFoundException.class)
    public ResponseEntity<String> handleConsultationNotFound(ConsultationNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
}
