package org.ust.project.controller;

import org.ust.project.dto.DoctorRequestDTO;
import org.ust.project.dto.DoctorResponseDTO;
import org.ust.project.service.DoctorService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/doctors")
@Valid
public class DoctorController {

    @Autowired
    private DoctorService doctorService;

    // Create a new doctor
    @PostMapping
    public ResponseEntity<DoctorResponseDTO> createDoctor(@RequestBody DoctorRequestDTO doctorRequestDTO) {
        DoctorResponseDTO doctorResponseDTO = doctorService.createDoctor(doctorRequestDTO);
        return new ResponseEntity<>(doctorResponseDTO, HttpStatus.CREATED);
    }

    // Get doctor by ID
    @GetMapping("/{id}")
    public ResponseEntity<DoctorResponseDTO> getDoctorById(@PathVariable Long id) {
        DoctorResponseDTO doctorResponseDTO = doctorService.getDoctorById(id);
        if (doctorResponseDTO != null) {
            return new ResponseEntity<>(doctorResponseDTO, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND); // Or handle not found case
    }

    // Get all doctors
    @GetMapping
    public ResponseEntity<List<DoctorResponseDTO>> getAllDoctors() {
        List<DoctorResponseDTO> doctorList = doctorService.getAllDoctors();
        return new ResponseEntity<>(doctorList, HttpStatus.OK);
    }

    // Update doctor details
    @PutMapping("/{id}")
    public ResponseEntity<DoctorResponseDTO> updateDoctor(@PathVariable Long id, @RequestBody DoctorRequestDTO doctorRequestDTO) {
        DoctorResponseDTO updatedDoctor = doctorService.updateDoctor(id, doctorRequestDTO);
        if (updatedDoctor != null) {
            return new ResponseEntity<>(updatedDoctor, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND); // Or handle not found case
    }

    // Delete doctor
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDoctor(@PathVariable Long id) {
        if (doctorService.deleteDoctor(id)) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // Successfully deleted
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND); // Doctor not found
    }
}
