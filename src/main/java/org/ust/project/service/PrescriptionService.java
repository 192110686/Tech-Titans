package org.ust.project.service;  // Corrected package name

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ust.project.dto.AppointmentResponseDTO;
import org.ust.project.dto.DoctorResponseDTO;
import org.ust.project.dto.MedicalRecordResponseDTO;
import org.ust.project.dto.PatientResponseDTO;
import org.ust.project.dto.PrescriptionRequestDTO;
import org.ust.project.dto.PrescriptionResponseDTO;
import org.ust.project.exception.AppointmentNotFoundException;
import org.ust.project.exception.MedicalRecordNotFoundException;
import org.ust.project.exception.PrescriptionNotFoundException;
import org.ust.project.model.Appointment;
import org.ust.project.model.Consultation;
import org.ust.project.model.InventoryItem;
import org.ust.project.model.MedicalRecord;
import org.ust.project.model.Prescription;
import org.ust.project.repo.AppointmentRepository;
import org.ust.project.repo.ConsultationRepository;
import org.ust.project.repo.InventoryItemRepository;
import org.ust.project.repo.MedicalRecordRepository;
import org.ust.project.repo.PrescriptionRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class PrescriptionService {

    @Autowired
    private PrescriptionRepository prescriptionRepository;

    @Autowired
    private ConsultationRepository consultationRepository;

    @Autowired
    private InventoryItemRepository inventoryItemRepository;

    /* ================= INVENTORY UPDATE ================= */
    private void checkAndUpdateInventory(Prescription prescription) {

        for (InventoryItem item : prescription.getInventoryItems()) {

            InventoryItem inventoryItem = inventoryItemRepository.findById(item.getId())
                    .orElseThrow(() ->
                            new RuntimeException("Medicine not found in inventory: " + item.getItemName()));

            if (inventoryItem.getQuantity() < item.getQuantity()) {
                throw new RuntimeException("Insufficient stock for: " + item.getItemName());
            }

            inventoryItem.setQuantity(
                    inventoryItem.getQuantity() - item.getQuantity()
            );

            inventoryItemRepository.save(inventoryItem);
        }
    }

    /* ================= CREATE ================= */
    public PrescriptionResponseDTO createPrescription(PrescriptionRequestDTO dto) {

        Consultation consultation = consultationRepository.findById(dto.getConsultationId())
                .orElseThrow(() ->
                        new RuntimeException("Consultation not found with id: " + dto.getConsultationId()));

        Prescription prescription = new Prescription();
        prescription.setMedicationName(dto.getMedicationName());
        prescription.setDosageMg(dto.getDosageMg());
        prescription.setPrice(dto.getPrice());
        prescription.setFrequency(dto.getFrequency());
        prescription.setStartDate(dto.getStartDate());
        prescription.setEndDate(dto.getEndDate());
        prescription.setConsultation(consultation);

        Prescription saved = prescriptionRepository.save(prescription);

        // 🔥 Update inventory after prescription creation
        checkAndUpdateInventory(saved);

        return toResponseDTO(saved);
    }

    /* ================= GET BY ID ================= */
    public PrescriptionResponseDTO getPrescriptionById(Long id) {

        Prescription prescription = prescriptionRepository.findById(id)
                .orElseThrow(() ->
                        new PrescriptionNotFoundException(id));

        return toResponseDTO(prescription);
    }

    /* ================= GET ALL ================= */
    public List<PrescriptionResponseDTO> getAllPrescriptions() {

        return prescriptionRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    /* ================= UPDATE ================= */
    public PrescriptionResponseDTO updatePrescription(Long id, PrescriptionRequestDTO dto) {

        Prescription prescription = prescriptionRepository.findById(id)
                .orElseThrow(() ->
                        new PrescriptionNotFoundException(id));

        Consultation consultation = consultationRepository.findById(dto.getConsultationId())
                .orElseThrow(() ->
                        new RuntimeException("Consultation not found with id: " + dto.getConsultationId()));

        prescription.setMedicationName(dto.getMedicationName());
        prescription.setDosageMg(dto.getDosageMg());
        prescription.setPrice(dto.getPrice());
        prescription.setFrequency(dto.getFrequency());
        prescription.setStartDate(dto.getStartDate());
        prescription.setEndDate(dto.getEndDate());
        prescription.setConsultation(consultation);

        Prescription updated = prescriptionRepository.save(prescription);

        return toResponseDTO(updated);
    }

    /* ================= DELETE ================= */
    public void deletePrescription(Long id) {

        if (!prescriptionRepository.existsById(id)) {
            throw new PrescriptionNotFoundException(id);
        }

        prescriptionRepository.deleteById(id);
    }

    /* ================= DTO MAPPER ================= */
    private PrescriptionResponseDTO toResponseDTO(Prescription prescription) {

        Consultation consultation = prescription.getConsultation();
        Appointment appointment = consultation.getAppointment();

        return new PrescriptionResponseDTO(
                prescription.getId(),
                prescription.getMedicationName(),
                prescription.getDosageMg(),
                prescription.getPrice(),
                prescription.getFrequency(),
                prescription.getStartDate(),
                prescription.getEndDate(),
                new AppointmentResponseDTO(
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
                )
        );
    }
}
