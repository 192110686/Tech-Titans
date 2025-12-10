package org.ust.project.controller;

import org.ust.project.model.Appointment;
import org.ust.project.service.AppointmentService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/appointments")
@CrossOrigin("*")
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    // GET All
    @GetMapping
    public List<Appointment> getAllAppointments() {
        return appointmentService.getAllAppointments();
    }

    // POST (Create)
    // We use a "Request Object" (DTO) here to make the JSON simple
    @PostMapping
    public Appointment createAppointment(@RequestBody AppointmentRequest request) {
        // Create the basic appointment object from the request
        Appointment appointment = new Appointment();
        appointment.setAppointmentDate(request.getAppointmentDate());
        appointment.setTimeSlot(request.getTimeSlot());
        appointment.setStatus(request.getStatus());

        // Call the service with IDs
        return appointmentService.createAppointment(
                request.getPatientId(), 
                request.getDoctorId(), 
                appointment
        );
    }

    // DELETE
    @DeleteMapping("/{id}")
    public void deleteAppointment(@PathVariable Long id) {
        appointmentService.deleteAppointment(id);
    }
}

// === Helper Class (DTO) ===
// Put this at the bottom of the file (or in a separate file if you prefer)
// This defines what the JSON input should look like.
@Data
class AppointmentRequest {
    private Long patientId;
    private Long doctorId;
    private java.time.LocalDate appointmentDate;
    private String timeSlot;
    private String status;
}