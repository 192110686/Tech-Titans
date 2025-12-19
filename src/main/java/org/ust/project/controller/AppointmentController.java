package org.ust.project.controller;  // Corrected package name

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.ust.project.dto.AppointmentRequestDTO;
import org.ust.project.dto.AppointmentResponseDTO;
import org.ust.project.service.AppointmentService;

@RestController
@RequestMapping("/appointments")  // Base URL for appointment-related endpoints
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    // Endpoint to create a new appointment
    @PostMapping
    public ResponseEntity<AppointmentResponseDTO> createAppointment(@RequestBody AppointmentRequestDTO appointmentRequestDTO) {
        AppointmentResponseDTO appointmentResponseDTO = appointmentService.createAppointment(appointmentRequestDTO);
        if (appointmentResponseDTO != null) {
            return ResponseEntity.ok(appointmentResponseDTO);
        }
        return ResponseEntity.badRequest().build();  // Return a bad request if the appointment is not created
    }

    // Endpoint to get an appointment by its ID
    @GetMapping("/{id}")
    public ResponseEntity<AppointmentResponseDTO> getAppointmentById(@PathVariable Long id) {
        AppointmentResponseDTO appointmentResponseDTO = appointmentService.getAppointmentById(id);
        if (appointmentResponseDTO != null) {
            return ResponseEntity.ok(appointmentResponseDTO);
        }
        return ResponseEntity.notFound().build();  // Return a 404 if the appointment is not found
    }

    // Endpoint to get all appointments
    @GetMapping
    public ResponseEntity<List<AppointmentResponseDTO>> getAllAppointments() {
        List<AppointmentResponseDTO> appointments = appointmentService.getAllAppointments();
        return ResponseEntity.ok(appointments);  // Return a list of appointments
    }

    // Endpoint to update an appointment
    @PutMapping("/{id}")
    public ResponseEntity<AppointmentResponseDTO> updateAppointment(@PathVariable Long id, @RequestBody AppointmentRequestDTO appointmentRequestDTO) {
        AppointmentResponseDTO appointmentResponseDTO = appointmentService.updateAppointment(id, appointmentRequestDTO);
        if (appointmentResponseDTO != null) {
            return ResponseEntity.ok(appointmentResponseDTO);
        }
        return ResponseEntity.notFound().build();  // Return a 404 if the appointment is not found
    }

    // Endpoint to delete an appointment
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAppointment(@PathVariable Long id) {
        boolean deleted = appointmentService.deleteAppointment(id);
        if (deleted) {
            return ResponseEntity.noContent().build();  // Return 204 No Content on successful deletion
        }
        return ResponseEntity.notFound().build();  // Return a 404 if the appointment is not found
    }
}
