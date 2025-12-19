package org.ust.project.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ust.project.dto.PrescriptionRequestDTO;
import org.ust.project.dto.PrescriptionResponseDTO;
import org.ust.project.model.Appointment;
import org.ust.project.model.MedicalRecord;
import org.ust.project.model.Prescription;
import org.ust.project.repo.AppointmentRepository;
import org.ust.project.repo.MedicalRecordRepository;
import org.ust.project.repo.PrescriptionRepository;

@Service
public class PrescriptionService {

    @Autowired
    private PrescriptionRepository prescriptionRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private MedicalRecordRepository medicalRecordRepository;

    // ───────────────────────────────────────────────
    // CREATE
    // ───────────────────────────────────────────────
    public PrescriptionResponseDTO createPrescription(PrescriptionRequestDTO dto) {

        Prescription prescription = new Prescription();
        prescription.setMedicationName(dto.getMedicationName());
        prescription.setDosageMg(dto.getDosageMg());
        prescription.setPrice(dto.getPrice());
        prescription.setFrequency(dto.getFrequency());
        prescription.setStartDate(dto.getStartDate());
        prescription.setEndDate(dto.getEndDate());

        // SET APPOINTMENT
        Appointment appointment = appointmentRepository.findById(dto.getAppointmentId())
                .orElseThrow(() -> new RuntimeException("Appointment not found"));
        prescription.setAppointment(appointment);

        // SET MEDICAL RECORD
        MedicalRecord medicalRecord = medicalRecordRepository.findById(dto.getMedicalRecordId())
                .orElseThrow(() -> new RuntimeException("Medical record not found"));
        prescription.setMedicalRecord(medicalRecord);

        // SAVE
        Prescription saved = prescriptionRepository.save(prescription);

        return convertToResponse(saved);
    }

    // ───────────────────────────────────────────────
    // GET BY ID
    // ───────────────────────────────────────────────
    public PrescriptionResponseDTO getPrescriptionById(Long id) {
        Prescription prescription = prescriptionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Prescription not found"));

        return convertToResponse(prescription);
    }

    // ───────────────────────────────────────────────
    // GET ALL
    // ───────────────────────────────────────────────
    public List<PrescriptionResponseDTO> getAllPrescriptions() {
        return prescriptionRepository.findAll()
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    // ───────────────────────────────────────────────
    // DELETE
    // ───────────────────────────────────────────────
    public void deletePrescription(Long id) {
        if (!prescriptionRepository.existsById(id)) {
            throw new RuntimeException("Prescription not found");
        }
        prescriptionRepository.deleteById(id);
    }

    // ───────────────────────────────────────────────
    // MAPPER
    // ───────────────────────────────────────────────
    private PrescriptionResponseDTO convertToResponse(Prescription prescription) {

        return new PrescriptionResponseDTO(
                prescription.getId(),
                prescription.getMedicationName(),
                prescription.getDosageMg(),
                prescription.getPrice(),
                prescription.getFrequency(),
                prescription.getStartDate(),
                prescription.getEndDate(),
                null,   // appointment DTO skipped
                null    // medical record DTO skipped
        );
    }
}