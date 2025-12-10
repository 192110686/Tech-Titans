package org.ust.project.controller;

import org.ust.project.model.MedicalRecord;
import org.ust.project.service.MedicalRecordService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/medical-records")
@CrossOrigin("*")
public class MedicalRecordController {

    @Autowired
    private MedicalRecordService medicalRecordService;

    @GetMapping
    public List<MedicalRecord> getAllMedicalRecords() {
        return medicalRecordService.getAllMedicalRecords();
    }

    @PostMapping
    public MedicalRecord createMedicalRecord(@RequestBody MedicalRecordRequest request) {
        MedicalRecord record = new MedicalRecord();
        record.setDiagnosis(request.getDiagnosis());
        record.setTreatmentPlan(request.getTreatmentPlan());
        record.setSymptoms(request.getSymptoms());
        
        // Pass IDs to service to handle the relationships
        return medicalRecordService.createMedicalRecord(
                request.getPatientId(),
                request.getDoctorId(),
                record
        );
    }

    @DeleteMapping("/{id}")
    public void deleteMedicalRecord(@PathVariable Long id) {
        medicalRecordService.deleteMedicalRecord(id);
    }
}

// Helper DTO for JSON input
@Data
class MedicalRecordRequest {
    private Long patientId;
    private Long doctorId;
    private String diagnosis;
    private String treatmentPlan;
    private String symptoms;
}