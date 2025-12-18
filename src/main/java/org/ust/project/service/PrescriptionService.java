package org.ust.project.service;  // Corrected package name

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ust.project.dto.PrescriptionRequestDTO;
import org.ust.project.dto.PrescriptionResponseDTO;
import org.ust.project.dto.AppointmentResponseDTO;
import org.ust.project.dto.MedicalRecordResponseDTO;
import org.ust.project.dto.DoctorResponseDTO;
import org.ust.project.dto.PatientResponseDTO;
import org.ust.project.model.Prescription;
import org.ust.project.model.Appointment;
import org.ust.project.model.MedicalRecord;
import org.ust.project.model.Doctor;
import org.ust.project.model.Patient;
import org.ust.project.repo.PrescriptionRepository;
import org.ust.project.repo.AppointmentRepository;
import org.ust.project.repo.MedicalRecordRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PrescriptionService {

    @Autowired
    private PrescriptionRepository prescriptionRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private MedicalRecordRepository medicalRecordRepository;

    // Create a new prescription
    public PrescriptionResponseDTO createPrescription(PrescriptionRequestDTO prescriptionRequestDTO) {
        Prescription prescription = new Prescription();

        // Fetch the Appointment and MedicalRecord from their respective repositories
        Optional<Appointment> appointmentOptional = appointmentRepository.findById(prescriptionRequestDTO.getAppointmentId());
        Optional<MedicalRecord> medicalRecordOptional = medicalRecordRepository.findById(prescriptionRequestDTO.getMedicalRecordId());

        if (appointmentOptional.isPresent() && medicalRecordOptional.isPresent()) {
            Appointment appointment = appointmentOptional.get();
            MedicalRecord medicalRecord = medicalRecordOptional.get();

            // Set the prescription details
            prescription.setMedicationName(prescriptionRequestDTO.getMedicationName());
            prescription.setDosageMg(prescriptionRequestDTO.getDosageMg());
            prescription.setPrice(prescriptionRequestDTO.getPrice());
            prescription.setFrequency(prescriptionRequestDTO.getFrequency());
            prescription.setStartDate(prescriptionRequestDTO.getStartDate());
            prescription.setEndDate(prescriptionRequestDTO.getEndDate());
            prescription.setAppointment(appointment);
            prescription.setMedicalRecord(medicalRecord);

            // Save the prescription
            prescription = prescriptionRepository.save(prescription);

            // Convert Appointment, MedicalRecord, Doctor, and Patient to their respective DTOs
            AppointmentResponseDTO appointmentResponseDTO = new AppointmentResponseDTO(
                appointment.getId(),
                appointment.getAppointmentDate(),
                new DoctorResponseDTO(
                    appointment.getDoctor().getId(),
                    appointment.getDoctor().getFirstName(),
                    appointment.getDoctor().getLastName(),
                    appointment.getDoctor().getSpecialization(),
                    appointment.getDoctor().getAvailabilitySchedule()
                ),
                new PatientResponseDTO(
                    appointment.getPatient().getId(),
                    appointment.getPatient().getFirstName(),
                    appointment.getPatient().getLastName(),
                    appointment.getPatient().getDateOfBirth(),
                    appointment.getPatient().getGender(),
                    appointment.getPatient().getPhoneNumber(),
                    appointment.getPatient().getEmail(),
                    appointment.getPatient().getBloodGroup()
                )
            );

            MedicalRecordResponseDTO medicalRecordResponseDTO = new MedicalRecordResponseDTO(
                medicalRecord.getId(),
                medicalRecord.getRecordDate(),
                medicalRecord.getDiagnosis(),
                medicalRecord.getTreatmentPlan(),
                medicalRecord.getSymptoms(),
                new DoctorResponseDTO(
                    medicalRecord.getDoctor().getId(),
                    medicalRecord.getDoctor().getFirstName(),
                    medicalRecord.getDoctor().getLastName(),
                    medicalRecord.getDoctor().getSpecialization(),
                    medicalRecord.getDoctor().getAvailabilitySchedule()
                ),
                new PatientResponseDTO(
                    medicalRecord.getPatient().getId(),
                    medicalRecord.getPatient().getFirstName(),
                    medicalRecord.getPatient().getLastName(),
                    medicalRecord.getPatient().getDateOfBirth(),
                    medicalRecord.getPatient().getGender(),
                    medicalRecord.getPatient().getPhoneNumber(),
                    medicalRecord.getPatient().getEmail(),
                    medicalRecord.getPatient().getBloodGroup()
                )
            );

            // Return the PrescriptionResponseDTO
            return new PrescriptionResponseDTO(
                prescription.getId(),
                prescription.getMedicationName(),
                prescription.getDosageMg(),
                prescription.getPrice(),
                prescription.getFrequency(),
                prescription.getStartDate(),
                prescription.getEndDate(),
                appointmentResponseDTO, // Nested Appointment DTO
                medicalRecordResponseDTO  // Nested MedicalRecord DTO
            );
        }

        return null; // Handle case where Appointment or MedicalRecord are not found
    }

    // Get prescription by ID
    public PrescriptionResponseDTO getPrescriptionById(Long id) {
        Optional<Prescription> prescriptionOptional = prescriptionRepository.findById(id);
        if (prescriptionOptional.isPresent()) {
            Prescription prescription = prescriptionOptional.get();

            // Convert Appointment, MedicalRecord, Doctor, and Patient to their respective DTOs
            AppointmentResponseDTO appointmentResponseDTO = new AppointmentResponseDTO(
                prescription.getAppointment().getId(),
                prescription.getAppointment().getAppointmentDate(),
                new DoctorResponseDTO(
                    prescription.getAppointment().getDoctor().getId(),
                    prescription.getAppointment().getDoctor().getFirstName(),
                    prescription.getAppointment().getDoctor().getLastName(),
                    prescription.getAppointment().getDoctor().getSpecialization(),
                    prescription.getAppointment().getDoctor().getAvailabilitySchedule()
                ),
                new PatientResponseDTO(
                    prescription.getAppointment().getPatient().getId(),
                    prescription.getAppointment().getPatient().getFirstName(),
                    prescription.getAppointment().getPatient().getLastName(),
                    prescription.getAppointment().getPatient().getDateOfBirth(),
                    prescription.getAppointment().getPatient().getGender(),
                    prescription.getAppointment().getPatient().getPhoneNumber(),
                    prescription.getAppointment().getPatient().getEmail(),
                    prescription.getAppointment().getPatient().getBloodGroup()
                )
            );

            MedicalRecordResponseDTO medicalRecordResponseDTO = new MedicalRecordResponseDTO(
                prescription.getMedicalRecord().getId(),
                prescription.getMedicalRecord().getRecordDate(),
                prescription.getMedicalRecord().getDiagnosis(),
                prescription.getMedicalRecord().getTreatmentPlan(),
                prescription.getMedicalRecord().getSymptoms(),
                new DoctorResponseDTO(
                    prescription.getMedicalRecord().getDoctor().getId(),
                    prescription.getMedicalRecord().getDoctor().getFirstName(),
                    prescription.getMedicalRecord().getDoctor().getLastName(),
                    prescription.getMedicalRecord().getDoctor().getSpecialization(),
                    prescription.getMedicalRecord().getDoctor().getAvailabilitySchedule()
                ),
                new PatientResponseDTO(
                    prescription.getMedicalRecord().getPatient().getId(),
                    prescription.getMedicalRecord().getPatient().getFirstName(),
                    prescription.getMedicalRecord().getPatient().getLastName(),
                    prescription.getMedicalRecord().getPatient().getDateOfBirth(),
                    prescription.getMedicalRecord().getPatient().getGender(),
                    prescription.getMedicalRecord().getPatient().getPhoneNumber(),
                    prescription.getMedicalRecord().getPatient().getEmail(),
                    prescription.getMedicalRecord().getPatient().getBloodGroup()
                )
            );

            // Return the PrescriptionResponseDTO
            return new PrescriptionResponseDTO(
                prescription.getId(),
                prescription.getMedicationName(),
                prescription.getDosageMg(),
                prescription.getPrice(),
                prescription.getFrequency(),
                prescription.getStartDate(),
                prescription.getEndDate(),
                appointmentResponseDTO,  // Nested Appointment DTO
                medicalRecordResponseDTO  // Nested MedicalRecord DTO
            );
        }
        return null; // Handle case where prescription is not found
    }

    // Get all prescriptions
    public List<PrescriptionResponseDTO> getAllPrescriptions() {
        List<Prescription> prescriptions = prescriptionRepository.findAll();
        return prescriptions.stream()
            .map(prescription -> {
                // Convert Appointment, MedicalRecord, Doctor, and Patient to their respective DTOs
                AppointmentResponseDTO appointmentResponseDTO = new AppointmentResponseDTO(
                    prescription.getAppointment().getId(),
                    prescription.getAppointment().getAppointmentDate(),
                    new DoctorResponseDTO(
                        prescription.getAppointment().getDoctor().getId(),
                        prescription.getAppointment().getDoctor().getFirstName(),
                        prescription.getAppointment().getDoctor().getLastName(),
                        prescription.getAppointment().getDoctor().getSpecialization(),
                        prescription.getAppointment().getDoctor().getAvailabilitySchedule()
                    ),
                    new PatientResponseDTO(
                        prescription.getAppointment().getPatient().getId(),
                        prescription.getAppointment().getPatient().getFirstName(),
                        prescription.getAppointment().getPatient().getLastName(),
                        prescription.getAppointment().getPatient().getDateOfBirth(),
                        prescription.getAppointment().getPatient().getGender(),
                        prescription.getAppointment().getPatient().getPhoneNumber(),
                        prescription.getAppointment().getPatient().getEmail(),
                        prescription.getAppointment().getPatient().getBloodGroup()
                    )
                );

                MedicalRecordResponseDTO medicalRecordResponseDTO = new MedicalRecordResponseDTO(
                    prescription.getMedicalRecord().getId(),
                    prescription.getMedicalRecord().getRecordDate(),
                    prescription.getMedicalRecord().getDiagnosis(),
                    prescription.getMedicalRecord().getTreatmentPlan(),
                    prescription.getMedicalRecord().getSymptoms(),
                    new DoctorResponseDTO(
                        prescription.getMedicalRecord().getDoctor().getId(),
                        prescription.getMedicalRecord().getDoctor().getFirstName(),
                        prescription.getMedicalRecord().getDoctor().getLastName(),
                        prescription.getMedicalRecord().getDoctor().getSpecialization(),
                        prescription.getMedicalRecord().getDoctor().getAvailabilitySchedule()
                    ),
                    new PatientResponseDTO(
                        prescription.getMedicalRecord().getPatient().getId(),
                        prescription.getMedicalRecord().getPatient().getFirstName(),
                        prescription.getMedicalRecord().getPatient().getLastName(),
                        prescription.getMedicalRecord().getPatient().getDateOfBirth(),
                        prescription.getMedicalRecord().getPatient().getGender(),
                        prescription.getMedicalRecord().getPatient().getPhoneNumber(),
                        prescription.getMedicalRecord().getPatient().getEmail(),
                        prescription.getMedicalRecord().getPatient().getBloodGroup()
                    )
                );

                // Return the PrescriptionResponseDTO
                return new PrescriptionResponseDTO(
                    prescription.getId(),
                    prescription.getMedicationName(),
                    prescription.getDosageMg(),
                    prescription.getPrice(),
                    prescription.getFrequency(),
                    prescription.getStartDate(),
                    prescription.getEndDate(),
                    appointmentResponseDTO,  // Nested Appointment DTO
                    medicalRecordResponseDTO  // Nested MedicalRecord DTO
                );
            })
            .collect(Collectors.toList());
    }

    // Update prescription
    public PrescriptionResponseDTO updatePrescription(Long id, PrescriptionRequestDTO prescriptionRequestDTO) {
        Optional<Prescription> prescriptionOptional = prescriptionRepository.findById(id);
        if (prescriptionOptional.isPresent()) {
            Prescription prescription = prescriptionOptional.get();

            Optional<Appointment> appointmentOptional = appointmentRepository.findById(prescriptionRequestDTO.getAppointmentId());
            Optional<MedicalRecord> medicalRecordOptional = medicalRecordRepository.findById(prescriptionRequestDTO.getMedicalRecordId());

            if (appointmentOptional.isPresent() && medicalRecordOptional.isPresent()) {
                prescription.setMedicationName(prescriptionRequestDTO.getMedicationName());
                prescription.setDosageMg(prescriptionRequestDTO.getDosageMg());
                prescription.setPrice(prescriptionRequestDTO.getPrice());
                prescription.setFrequency(prescriptionRequestDTO.getFrequency());
                prescription.setStartDate(prescriptionRequestDTO.getStartDate());
                prescription.setEndDate(prescriptionRequestDTO.getEndDate());
                prescription.setAppointment(appointmentOptional.get());
                prescription.setMedicalRecord(medicalRecordOptional.get());

                prescription = prescriptionRepository.save(prescription);

                // Convert Appointment and MedicalRecord to their respective DTOs
                AppointmentResponseDTO appointmentResponseDTO = new AppointmentResponseDTO(
                    appointmentOptional.get().getId(),
                    appointmentOptional.get().getAppointmentDate(),
                    new DoctorResponseDTO(
                        appointmentOptional.get().getDoctor().getId(),
                        appointmentOptional.get().getDoctor().getFirstName(),
                        appointmentOptional.get().getDoctor().getLastName(),
                        appointmentOptional.get().getDoctor().getSpecialization(),
                        appointmentOptional.get().getDoctor().getAvailabilitySchedule()
                    ),
                    new PatientResponseDTO(
                        appointmentOptional.get().getPatient().getId(),
                        appointmentOptional.get().getPatient().getFirstName(),
                        appointmentOptional.get().getPatient().getLastName(),
                        appointmentOptional.get().getPatient().getDateOfBirth(),
                        appointmentOptional.get().getPatient().getGender(),
                        appointmentOptional.get().getPatient().getPhoneNumber(),
                        appointmentOptional.get().getPatient().getEmail(),
                        appointmentOptional.get().getPatient().getBloodGroup()
                    )
                );

                MedicalRecordResponseDTO medicalRecordResponseDTO = new MedicalRecordResponseDTO(
                    medicalRecordOptional.get().getId(),
                    medicalRecordOptional.get().getRecordDate(),
                    medicalRecordOptional.get().getDiagnosis(),
                    medicalRecordOptional.get().getTreatmentPlan(),
                    medicalRecordOptional.get().getSymptoms(),
                    new DoctorResponseDTO(
                        medicalRecordOptional.get().getDoctor().getId(),
                        medicalRecordOptional.get().getDoctor().getFirstName(),
                        medicalRecordOptional.get().getDoctor().getLastName(),
                        medicalRecordOptional.get().getDoctor().getSpecialization(),
                        medicalRecordOptional.get().getDoctor().getAvailabilitySchedule()
                    ),
                    new PatientResponseDTO(
                        medicalRecordOptional.get().getPatient().getId(),
                        medicalRecordOptional.get().getPatient().getFirstName(),
                        medicalRecordOptional.get().getPatient().getLastName(),
                        medicalRecordOptional.get().getPatient().getDateOfBirth(),
                        medicalRecordOptional.get().getPatient().getGender(),
                        medicalRecordOptional.get().getPatient().getPhoneNumber(),
                        medicalRecordOptional.get().getPatient().getEmail(),
                        medicalRecordOptional.get().getPatient().getBloodGroup()
                    )
                );

                // Return the PrescriptionResponseDTO
                return new PrescriptionResponseDTO(
                    prescription.getId(),
                    prescription.getMedicationName(),
                    prescription.getDosageMg(),
                    prescription.getPrice(),
                    prescription.getFrequency(),
                    prescription.getStartDate(),
                    prescription.getEndDate(),
                    appointmentResponseDTO,  // Nested Appointment DTO
                    medicalRecordResponseDTO  // Nested MedicalRecord DTO
                );
            }
        }
        return null; // Handle cases where Prescription, Appointment, or MedicalRecord are not found
    }

    // Delete prescription
    public boolean deletePrescription(Long id) {
        if (prescriptionRepository.existsById(id)) {
            prescriptionRepository.deleteById(id);
            return true;
        }
        return false; // Handle case where prescription is not found
    }
}
