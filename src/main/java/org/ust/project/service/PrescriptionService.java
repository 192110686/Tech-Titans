package org.ust.project.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.ust.project.dto.InventoryItemResponseDTO;
import org.ust.project.dto.PrescriptionRequestDTO;
import org.ust.project.dto.PrescriptionResponseDTO;
import org.ust.project.exception.ConsultationNotFoundException;
import org.ust.project.exception.InventoryItemNotFoundException;
import org.ust.project.exception.PartialStockFulfilledException;
import org.ust.project.exception.PrescriptionNotFoundException;
import org.ust.project.model.Consultation;
import org.ust.project.model.InventoryItem;
import org.ust.project.model.Prescription;
import org.ust.project.repo.ConsultationRepository;
import org.ust.project.repo.PrescriptionRepository;
import org.ust.project.repo.InventoryItemRepository;

@Service
@Transactional
public class PrescriptionService {

    private final PrescriptionRepository prescriptionRepository;
    private final ConsultationRepository consultationRepository;
    private final InventoryItemRepository inventoryItemRepository;

    @Autowired
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

        // Fetch Consultation
        Consultation consultation = consultationRepository.findById(dto.getConsultationId())
                .orElseThrow(() -> new ConsultationNotFoundException(dto.getConsultationId()));

        // Create a new Prescription
        Prescription prescription = new Prescription();
        prescription.setMedicationName(dto.getMedicationName());
        prescription.setDosageMg(dto.getDosageMg());
        prescription.setPrice(dto.getPrice());
        prescription.setFrequency(dto.getFrequency());
        prescription.setStartDate(dto.getStartDate());
        prescription.setEndDate(dto.getEndDate());
        prescription.setConsultation(consultation);

        // Link prescription with inventory items
        List<InventoryItem> inventoryItems = inventoryItemRepository.findByItemName(dto.getMedicationName());
        prescription.setInventoryItems(inventoryItems);

        // Save the Prescription
        Prescription saved = prescriptionRepository.save(prescription);

        // Maintain bidirectional consistency with Consultation
        consultation.setPrescription(saved);

        // Update inventory stock and calculate total price
        try {
            updateInventoryStockAndCalculateTotalPrice(saved);
        } catch (PartialStockFulfilledException ex) {
            // Do not rollback transaction if partial stock is fulfilled
            return toResponseDTO(saved);
        }

        return toResponseDTO(saved);
    }

    /* ================= INVENTORY UPDATE AND CALCULATE PRESCRIPTION PRICE ================= */
    private void updateInventoryStockAndCalculateTotalPrice(Prescription prescription) {
        double totalPrice = 0.0;

        for (InventoryItem item : prescription.getInventoryItems()) {

            InventoryItem stockItem = inventoryItemRepository.findById(item.getId())
                    .orElseThrow(() -> new InventoryItemNotFoundException(item.getId()));

            double prescribedQty = item.getQuantity();
            double availableQty = stockItem.getQuantity();

            if (availableQty <= 0) {
                throw new PartialStockFulfilledException("No stock available for " + item.getItemName());
            }

            if (prescribedQty > availableQty) {
                // Partial fulfillment if prescribed quantity is more than available stock
                item.setQuantity(availableQty);
                stockItem.setQuantity(0.0);

                totalPrice += stockItem.getUnitPrice() * availableQty;

                inventoryItemRepository.save(stockItem);

                throw new PartialStockFulfilledException(
                        "Only " + availableQty + " units of " + item.getItemName() + " available. Billed partially."
                );
            }

            // Full fulfillment
            stockItem.setQuantity(availableQty - prescribedQty);
            inventoryItemRepository.save(stockItem);

            // Calculate price
            totalPrice += stockItem.getUnitPrice() * prescribedQty;
        }

        // Set the calculated total price for the prescription
        prescription.setPrice(totalPrice);
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

        // Inventory is NOT adjusted on update, so skipping inventory update here
        return toResponseDTO(updated);
    }

    /* ================= DELETE ================= */
    public void deletePrescription(Long id) {
        Prescription prescription = prescriptionRepository.findById(id)
                .orElseThrow(() -> new PrescriptionNotFoundException(id));
        prescriptionRepository.delete(prescription);
    }

    /* ================= DTO MAPPER ================= */
    private PrescriptionResponseDTO toResponseDTO(Prescription prescription) {

        List<InventoryItemResponseDTO> items = prescription.getInventoryItems() == null
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
                prescription.getPrice(),  // This now holds the total price
                prescription.getFrequency(),
                prescription.getStartDate(),
                prescription.getEndDate(),
                items
        );
    }
}
