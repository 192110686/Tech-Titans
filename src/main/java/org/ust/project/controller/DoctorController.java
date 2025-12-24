package org.ust.project.controller;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.ust.project.dto.DoctorRequestDTO;
import org.ust.project.dto.DoctorResponseDTO;
import org.ust.project.service.DoctorService;

@RestController
@RequestMapping("/doctors")
public class DoctorController {

    @Autowired
    private DoctorService doctorService;

    /* ================= CREATE ================= */
    @PostMapping
    public ResponseEntity<DoctorResponseDTO> createDoctor(
            @Valid @RequestBody DoctorRequestDTO doctorRequestDTO) {

        DoctorResponseDTO response =
                doctorService.createDoctor(doctorRequestDTO);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    /* ================= GET BY ID ================= */
    @GetMapping("/{id}")
    public ResponseEntity<DoctorResponseDTO> getDoctorById(
            @PathVariable Long id) {

        DoctorResponseDTO response =
                doctorService.getDoctorById(id);

        return ResponseEntity.ok(response);
    }

    /* ================= GET ALL ================= */
    @GetMapping
    public ResponseEntity<List<DoctorResponseDTO>> getAllDoctors() {
        List<DoctorResponseDTO> responses =
                doctorService.getAllDoctors();

        return ResponseEntity.ok(responses);
    }

    /* ================= UPDATE ================= */
    @PutMapping("/{id}")
    public ResponseEntity<DoctorResponseDTO> updateDoctor(
            @PathVariable Long id,
            @Valid @RequestBody DoctorRequestDTO doctorRequestDTO) {

        DoctorResponseDTO response =
                doctorService.updateDoctor(id, doctorRequestDTO);

        return ResponseEntity.ok(response);
    }

    /* ================= DELETE ================= */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDoctor(@PathVariable Long id) {

        doctorService.deleteDoctor(id);

        return ResponseEntity.noContent().build();
    }

    /* ================= CHECK DOCTOR AVAILABILITY ================= */
    @GetMapping("/{doctorId}/availability")
    public ResponseEntity<List<LocalDateTime>> checkDoctorAvailability(
            @PathVariable Long doctorId,
            @RequestParam LocalDateTime startTime,
            @RequestParam LocalDateTime endTime) {

        List<LocalDateTime> availableSlots = doctorService.getAvailableSlots(doctorId, startTime, endTime);
        return ResponseEntity.ok(availableSlots);
    }
}
