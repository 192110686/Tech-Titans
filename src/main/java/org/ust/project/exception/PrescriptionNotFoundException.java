package org.ust.project.exception;

public class PrescriptionNotFoundException extends RuntimeException {

    public PrescriptionNotFoundException(Long PrescriptionId) {
        super("Appointment not found with ID: " + PrescriptionId);
    }
}
