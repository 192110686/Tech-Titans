package org.ust.project.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.ust.project.dto.InventoryItemResponseDTO;
import org.ust.project.dto.PrescriptionRequestDTO;
import org.ust.project.dto.PrescriptionResponseDTO;
import org.ust.project.exception.ConsultationNotFoundException;
import org.ust.project.exception.InsufficientStockException;
import org.ust.project.exception.InventoryItemNotFoundException;
import org.ust.project.exception.PrescriptionNotFoundException;
import org.ust.project.model.Consultation;
import org.ust.project.model.InventoryItem;
import org.ust.project.model.Prescription;
import org.ust.project.repo.ConsultationRepository;
import org.ust.project.repo.InventoryItemRepository;
import org.ust.project.repo.PrescriptionRepository;

@Service
@Transactional
public class PrescriptionService {

    private final PrescriptionRepository prescriptionRepository;
    private final ConsultationRepository consultationRepository;
    private final InventoryItemRepository inventoryItemRepository;

    public PrescriptionService(
            PrescriptionRepository prescriptionRepository,
            ConsultationRepository consultationRepository,
            InventoryItemRepository inventoryItemRepository) {
        this.prescriptionRepository = prescriptionRepository;
        this.consultationRepository = consultationRepository;
        this.inventoryItemRepository = inventoryItemRepository;
    }

    /* ================= CREATE ================= */
    public PrescriptionResponseDTO createPrescription(PrescriptionRequestDTO dto) {

        Consultation consultation = consultationRepository.findById(dto.getConsultationId())
                .orElseThrow(() -> new ConsultationNotFoundException(dto.getConsultationId()));

        Prescription prescription = new Prescription();
        prescription.setMedicationName(dto.getMedicationName());
        prescription.setDosageMg(dto.getDosageMg());
        prescription.setPrice(dto.getPrice());
        prescription.setFrequency(dto.getFrequency());
        prescription.setStartDate(dto.getStartDate());
        prescription.setEndDate(dto.getEndDate());
        prescription.setConsultation(consultation);

        Prescription saved = prescriptionRepository.save(prescription);

        // 🔗 Maintain bidirectional consistency
        consultation.setPrescription(saved);

        // 🔥 Inventory update happens ONLY ON CREATE
        updateInventoryStock(saved);

        return toResponseDTO(saved);
    }

    /* ================= GET BY ID ================= */
    public PrescriptionResponseDTO getPrescriptionById(Long id) {

        Prescription prescription = prescriptionRepository.findById(id)
                .orElseThrow(() -> new PrescriptionNotFoundException(id));

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
                .orElseThrow(() -> new PrescriptionNotFoundException(id));

        prescription.setMedicationName(dto.getMedicationName());
        prescription.setDosageMg(dto.getDosageMg());
        prescription.setPrice(dto.getPrice());
        prescription.setFrequency(dto.getFrequency());
        prescription.setStartDate(dto.getStartDate());
        prescription.setEndDate(dto.getEndDate());

        Prescription updated = prescriptionRepository.save(prescription);

        // ❗ Inventory is NOT re-adjusted on update
        return toResponseDTO(updated);
    }

    /* ================= DELETE ================= */
    public void deletePrescription(Long id) {

        Prescription prescription = prescriptionRepository.findById(id)
                .orElseThrow(() -> new PrescriptionNotFoundException(id));

        prescriptionRepository.delete(prescription);
    }

    /* ================= INVENTORY UPDATE ================= */
    private void updateInventoryStock(Prescription prescription) {

        for (InventoryItem item : prescription.getInventoryItems()) {

            InventoryItem stockItem = inventoryItemRepository.findById(item.getId())
                    .orElseThrow(() ->
                            new InventoryItemNotFoundException(item.getId()));

            if (stockItem.getQuantity() < item.getQuantity()) {
                throw new InsufficientStockException(item.getItemName());
            }

            stockItem.setQuantity(
                    stockItem.getQuantity() - item.getQuantity()
            );

            inventoryItemRepository.save(stockItem);
        }
    }

    /* ================= DTO MAPPER ================= */
    private PrescriptionResponseDTO toResponseDTO(Prescription prescription) {

        List<InventoryItemResponseDTO> items =
            prescription.getInventoryItems() == null
                ? List.of()
                : prescription.getInventoryItems()
                    .stream()
                    .map(item -> new InventoryItemResponseDTO(
                            item.getId(),
                            item.getItemName(),
                            item.getCategory(),
                            item.getUnitPrice()
                    ))
                    .collect(Collectors.toList());

        return new PrescriptionResponseDTO(
                prescription.getId(),
                prescription.getMedicationName(),
                prescription.getDosageMg(),
                prescription.getPrice(),
                prescription.getFrequency(),
                prescription.getStartDate(),
                prescription.getEndDate(),
                items
        );
    }

}
