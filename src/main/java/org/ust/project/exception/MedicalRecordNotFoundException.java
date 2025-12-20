package org.ust.project.exception;

public class MedicalRecordNotFoundException extends RuntimeException {

    public MedicalRecordNotFoundException(Long medicalRecordId) {
        super("MedicalRecord not found with ID: " + medicalRecordId);
    }
}
