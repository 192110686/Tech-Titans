package org.ust.project.service;  // Corrected package name

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ust.project.dto.AppointmentRequestDTO;
import org.ust.project.dto.AppointmentResponseDTO;
import org.ust.project.dto.DoctorResponseDTO;
import org.ust.project.dto.PatientResponseDTO;
import org.ust.project.model.Appointment;
import org.ust.project.model.Doctor;
import org.ust.project.model.Patient;
import org.ust.project.repo.AppointmentRepository;
import org.ust.project.repo.DoctorRepository;
import org.ust.project.repo.PatientRepository;

@Service
public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private PatientRepository patientRepository;

    // Create a new appointment
    public AppointmentResponseDTO createAppointment(AppointmentRequestDTO appointmentRequestDTO) {
        Appointment appointment = new Appointment();

        // Fetch the Doctor and Patient from their respective repositories using their IDs
        Optional<Doctor> doctorOptional = doctorRepository.findById(appointmentRequestDTO.getDoctorId());
        Optional<Patient> patientOptional = patientRepository.findById(appointmentRequestDTO.getPatientId());

        if (doctorOptional.isPresent() && patientOptional.isPresent()) {
            Doctor doctor = doctorOptional.get();
            Patient patient = patientOptional.get();

            // Set the doctor, patient, and appointment date
            appointment.setDoctor(doctor);
            appointment.setPatient(patient);
            appointment.setAppointmentDate(appointmentRequestDTO.getAppointmentDate());

            // Save the appointment and return the response DTO with mapped Doctor and Patient DTOs
            appointment = appointmentRepository.save(appointment);
            return new AppointmentResponseDTO(
                appointment.getId(),
                appointment.getAppointmentDate(),
                new DoctorResponseDTO(
                    doctor.getId(),
                    doctor.getFirstName(),
                    doctor.getLastName(),
                    doctor.getSpecialization(),
                    doctor.getAvailabilitySchedule()
                ), // Doctor mapped to DoctorResponseDTO
                new PatientResponseDTO(
                    patient.getId(),
                    patient.getFirstName(),
                    patient.getLastName(),
                    patient.getDateOfBirth(),
                    patient.getGender(),
                    patient.getPhoneNumber(),
                    patient.getEmail(),
                    patient.getBloodGroup()
                ) // Patient mapped to PatientResponseDTO
            );
        }
        return null; // Handle case where doctor or patient is not found (could throw an exception)
    }

    // Get appointment by ID
    public AppointmentResponseDTO getAppointmentById(Long id) {
        Optional<Appointment> appointmentOptional = appointmentRepository.findById(id);
        if (appointmentOptional.isPresent()) {
            Appointment appointment = appointmentOptional.get();

            // Convert Doctor and Patient entities to their respective DTOs
            return new AppointmentResponseDTO(
                appointment.getId(),
                appointment.getAppointmentDate(),
                new DoctorResponseDTO(
                    appointment.getDoctor().getId(),
                    appointment.getDoctor().getFirstName(),
                    appointment.getDoctor().getLastName(),
                    appointment.getDoctor().getSpecialization(),
                    appointment.getDoctor().getAvailabilitySchedule()
                ), // Doctor mapped to DoctorResponseDTO
                new PatientResponseDTO(
                    appointment.getPatient().getId(),
                    appointment.getPatient().getFirstName(),
                    appointment.getPatient().getLastName(),
                    appointment.getPatient().getDateOfBirth(),
                    appointment.getPatient().getGender(),
                    appointment.getPatient().getPhoneNumber(),
                    appointment.getPatient().getEmail(),
                    appointment.getPatient().getBloodGroup()
                ) // Patient mapped to PatientResponseDTO
            );
        }
        return null; // Handle case where appointment is not found (could throw an exception)
    }

    // Get all appointments
    public List<AppointmentResponseDTO> getAllAppointments() {
        List<Appointment> appointments = appointmentRepository.findAll();
        return appointments.stream()
            .map(appointment -> new AppointmentResponseDTO(
                appointment.getId(),
                appointment.getAppointmentDate(),
                new DoctorResponseDTO(
                    appointment.getDoctor().getId(),
                    appointment.getDoctor().getFirstName(),
                    appointment.getDoctor().getLastName(),
                    appointment.getDoctor().getSpecialization(),
                    appointment.getDoctor().getAvailabilitySchedule()
                ), // Doctor mapped to DoctorResponseDTO
                new PatientResponseDTO(
                    appointment.getPatient().getId(),
                    appointment.getPatient().getFirstName(),
                    appointment.getPatient().getLastName(),
                    appointment.getPatient().getDateOfBirth(),
                    appointment.getPatient().getGender(),
                    appointment.getPatient().getPhoneNumber(),
                    appointment.getPatient().getEmail(),
                    appointment.getPatient().getBloodGroup()
                ) // Patient mapped to PatientResponseDTO
            ))
            .collect(Collectors.toList());
    }

    // Update an appointment
    public AppointmentResponseDTO updateAppointment(Long id, AppointmentRequestDTO appointmentRequestDTO) {
        Optional<Appointment> appointmentOptional = appointmentRepository.findById(id);
        if (appointmentOptional.isPresent()) {
            Appointment appointment = appointmentOptional.get();

            Optional<Doctor> doctorOptional = doctorRepository.findById(appointmentRequestDTO.getDoctorId());
            Optional<Patient> patientOptional = patientRepository.findById(appointmentRequestDTO.getPatientId());

            if (doctorOptional.isPresent() && patientOptional.isPresent()) {
                appointment.setDoctor(doctorOptional.get());
                appointment.setPatient(patientOptional.get());
                appointment.setAppointmentDate(appointmentRequestDTO.getAppointmentDate());

                appointment = appointmentRepository.save(appointment);
                return new AppointmentResponseDTO(
                    appointment.getId(),
                    appointment.getAppointmentDate(),
                    new DoctorResponseDTO(
                        appointment.getDoctor().getId(),
                        appointment.getDoctor().getFirstName(),
                        appointment.getDoctor().getLastName(),
                        appointment.getDoctor().getSpecialization(),
                        appointment.getDoctor().getAvailabilitySchedule()
                    ), // Doctor mapped to DoctorResponseDTO
                    new PatientResponseDTO(
                        appointment.getPatient().getId(),
                        appointment.getPatient().getFirstName(),
                        appointment.getPatient().getLastName(),
                        appointment.getPatient().getDateOfBirth(),
                        appointment.getPatient().getGender(),
                        appointment.getPatient().getPhoneNumber(),
                        appointment.getPatient().getEmail(),
                        appointment.getPatient().getBloodGroup()
                    ) // Patient mapped to PatientResponseDTO
                );
            }
        }
        return null; // Handle case where appointment or doctor/patient is not found
    }

    // Delete an appointment
    public boolean deleteAppointment(Long id) {
        if (appointmentRepository.existsById(id)) {
            appointmentRepository.deleteById(id);
            return true;
        }
        return false; // Handle case where appointment is not found
    }
}
