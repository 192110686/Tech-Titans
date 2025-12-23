package org.ust.project.service;

import org.ust.project.model.Consultation;
import org.ust.project.model.Prescription;
import org.ust.project.repo.ConsultationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConsultationService {
	
	@Autowired
    private ConsultationRepository consultationRepository;

    @Autowired
    private PrescriptionService prescriptionService;

    @Autowired
    private InventoryItemService inventoryItemService;
    
    ConsultationService(ConsultationRepository consultationRepository) {
        this.consultationRepository = consultationRepository;
    }

    // Create Consultation and Link to Prescription and Bill
    public Consultation createConsultation(Consultation consultation) {
        // Create and save Consultation (as usual)
        consultation = consultationRepository.save(consultation);

        // Check and update inventory based on the prescription (if any)
        if (consultation.getPrescription() != null) {
            // Update inventory with prescribed medicines
            inventoryItemService.checkAndUpdateInventory(consultation.getPrescription());
        }

        return consultation;
    }
}
