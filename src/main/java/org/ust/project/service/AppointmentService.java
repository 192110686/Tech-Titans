package org.ust.project.service;

import org.ust.project.model.Appointment;
import org.ust.project.model.Doctor;
import org.ust.project.model.Patient;
import org.ust.project.repo.AppointmentRepository;
import org.ust.project.repo.DoctorRepository;
import org.ust.project.repo.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    // 1. Get All Appointments
    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }

    // 2. Get Appointment by ID
    public Optional<Appointment> getAppointmentById(Long id) {
        return appointmentRepository.findById(id);
    }

    // 3. Create Appointment (The "Tricky" Part)
    // We take the raw IDs, find the real Patient/Doctor, and then save.
    public Appointment createAppointment(Long patientId, Long doctorId, Appointment appointment) {
        // Find the Patient
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found with ID: " + patientId));

        // Find the Doctor
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new RuntimeException("Doctor not found with ID: " + doctorId));

        // Connect them to the appointment
        appointment.setPatient(patient);
        appointment.setDoctor(doctor);
        
        // Default status if not provided
        if (appointment.getStatus() == null) {
            appointment.setStatus("Scheduled");
        }

        return appointmentRepository.save(appointment);
    }

    // 4. Delete Appointment
    public void deleteAppointment(Long id) {
        appointmentRepository.deleteById(id);
    }
}