package org.ust.project.exception;

public class PatientEntityNotFoundException extends RuntimeException {

    public PatientEntityNotFoundException(Long patientId) {
        super(String.format("Patient with ID %d not found", patientId));
    }
}
