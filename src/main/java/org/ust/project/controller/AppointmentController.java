package org.ust.project.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.ust.project.dto.AppointmentRequestDTO;
import org.ust.project.dto.AppointmentResponseDTO;
import org.ust.project.dto.RescheduleRequestDTO;
import org.ust.project.service.AppointmentService;

import jakarta.validation.Valid;

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
   // In AppointmentController.java
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
  // Reschedule appointment endpoint using query parameter
// Reschedule appointment endpoint using request body
@PutMapping("/{id}/reschedule")
public ResponseEntity<String> rescheduleAppointment(@PathVariable Long id, @RequestBody RescheduleRequestDTO rescheduleRequest) {
    try {
        LocalDateTime newDateTime = LocalDateTime.parse(rescheduleRequest.getNewAppointmentDateTime()); 
        appointmentService.rescheduleAppointment(id, newDateTime);
        return ResponseEntity.ok("Appointment rescheduled to " + newDateTime);
    } catch (Exception e) {
        return ResponseEntity.badRequest().body("Invalid date format. Please provide the date in 'yyyy-MM-dd'T'HH:mm:ss' format.");
    }
}



@PutMapping("/{id}/cancel")
public ResponseEntity<String> cancelAppointment(@PathVariable Long id) {
    appointmentService.cancelAppointment(id);
    return ResponseEntity.ok("Appointment with ID " + id + " has been cancelled.");
}


}
