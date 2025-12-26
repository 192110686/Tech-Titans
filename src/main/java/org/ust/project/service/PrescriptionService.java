package org.ust.project.service;

import java.util.List;
import java.util.stream.Collectors;

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
        prescription.setFrequency(dto.getFrequency());
        prescription.setStartDate(dto.getStartDate());
        prescription.setEndDate(dto.getEndDate());
        prescription.setConsultation(consultation);
        
        if (dto.getInventoryItems() != null) {
            List<InventoryItem> items =
                    dto.getInventoryItems().stream()
                            .map(reqItem -> inventoryItemRepository.findById(reqItem.getId())
                                    .orElseThrow(() ->
                                            new InventoryItemNotFoundException(reqItem.getId())))
                            .collect(Collectors.toList());

            prescription.setInventoryItems(items);
        }

        Prescription saved = prescriptionRepository.save(prescription);

        consultation.setPrescription(saved);

        // ▶️ Calculate price using new rule
        calculatePrescriptionPrice(saved);

        return toResponseDTO(saved);
    }

    private void calculatePrescriptionPrice(Prescription prescription) {

        double totalPrice = 0.0;

        // Number of days between start & end date (inclusive)
        long days = java.time.temporal.ChronoUnit.DAYS
                .between(prescription.getStartDate(), prescription.getEndDate()) + 1;

        for (InventoryItem item : prescription.getInventoryItems()) {

            InventoryItem stockItem = inventoryItemRepository.findById(item.getId())
                    .orElseThrow(() -> new InventoryItemNotFoundException(item.getId()));

            double unitPrice = stockItem.getUnitPrice();
            double frequencyPerDay = prescription.getFrequency();

            double medicinePrice = unitPrice * frequencyPerDay * days;

            totalPrice += medicinePrice;
        }

        prescription.setPrice(totalPrice);
        prescriptionRepository.save(prescription);
    }

    
    private void updateInventoryStockAndCalculateTotalPrice(Prescription prescription) {

        // If no inventory items are attached, KEEP the manually-entered price
        if (prescription.getInventoryItems() == null ||
            prescription.getInventoryItems().isEmpty()) {
            return;
        }

        double totalPrice = 0.0;

        for (InventoryItem item : prescription.getInventoryItems()) {

            InventoryItem stockItem = inventoryItemRepository.findById(item.getId())
                    .orElseThrow(() -> new InventoryItemNotFoundException(item.getId()));

            double prescribedQty = item.getQuantity();
            double availableQty = stockItem.getQuantity();

            if (availableQty <= 0) {
                throw new PartialStockFulfilledException(
                        "No stock available for " + item.getItemName()
                );
            }

            // 🔹 Partial fulfillment
            if (prescribedQty > availableQty) {

                item.setQuantity(availableQty);   // bill only available qty
                stockItem.setQuantity(0.0);
                totalPrice += stockItem.getUnitPrice() * availableQty;

                inventoryItemRepository.save(stockItem);

                throw new PartialStockFulfilledException(
                        "Only " + availableQty + " units of "
                        + item.getItemName() + " available. Billed partially."
                );
            }

            // 🔹 Full fulfillment
            stockItem.setQuantity(availableQty - prescribedQty);
            inventoryItemRepository.save(stockItem);

            totalPrice += stockItem.getUnitPrice() * prescribedQty;
        }

        // overwrite price ONLY when inventory billing is applied
        prescription.setPrice(totalPrice);
        prescriptionRepository.save(prescription);
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
                prescription.getPrice(),  // This now holds the total price
                prescription.getFrequency(),
                prescription.getStartDate(),
                prescription.getEndDate(),
                items
        );
    }
}
