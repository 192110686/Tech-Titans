package org.ust.project.controller;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.ust.project.dto.AppointmentRequestDTO;
import org.ust.project.dto.AppointmentResponseDTO;
import org.ust.project.model.AppointmentStatus;
import org.ust.project.service.AppointmentService;

@RestController
@RequestMapping("/appointments")
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    /* ================= CREATE ================= */
    @PostMapping
    public ResponseEntity<AppointmentResponseDTO> createAppointment(
            @Valid @RequestBody AppointmentRequestDTO requestDTO) {

        AppointmentResponseDTO response =
                appointmentService.createAppointment(requestDTO);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    /* ================= GET BY ID ================= */
    @GetMapping("/{id}")
    public ResponseEntity<AppointmentResponseDTO> getAppointmentById(
            @PathVariable Long id) {

        AppointmentResponseDTO response =
                appointmentService.getAppointmentById(id);

        return ResponseEntity.ok(response);
    }

    /* ================= GET ALL ================= */
    @GetMapping
    public ResponseEntity<List<AppointmentResponseDTO>> getAllAppointments() {

        List<AppointmentResponseDTO> responses =
                appointmentService.getAllAppointments();

        return ResponseEntity.ok(responses);
    }

    /* ================= UPDATE ================= */
    @PutMapping("/{id}")
    public ResponseEntity<AppointmentResponseDTO> updateAppointment(
            @PathVariable Long id,
            @Valid @RequestBody AppointmentRequestDTO requestDTO) {

        AppointmentResponseDTO response =
                appointmentService.updateAppointment(id, requestDTO);

        return ResponseEntity.ok(response);
    }

    /* ================= DELETE ================= */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAppointment(@PathVariable Long id) {

        appointmentService.deleteAppointment(id);

        return ResponseEntity.noContent().build();
    }

    /* ================= BOOK APPOINTMENT ================= */
    @PostMapping("/book")
    public ResponseEntity<String> bookAppointment(@RequestBody AppointmentRequestDTO appointmentRequest) {
        String response = appointmentService.bookAppointment(appointmentRequest);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

     @PutMapping("/{id}/status")
    public ResponseEntity<String> updateAppointmentStatus(@PathVariable Long id, @RequestParam String status) {
        appointmentService.updateAppointmentStatus(id, status);
        return ResponseEntity.ok("Appointment status updated to " + status);
    }

    // Endpoint to reschedule an appointment
    @PutMapping("/{id}/reschedule")
    public ResponseEntity<String> rescheduleAppointment(@PathVariable Long id, @RequestParam String newAppointmentDateTime) {
        LocalDateTime newDateTime = LocalDateTime.parse(newAppointmentDateTime);
        appointmentService.rescheduleAppointment(id, newDateTime);
        return ResponseEntity.ok("Appointment rescheduled to " + newDateTime);
    }
}
