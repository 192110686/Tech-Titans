package org.ust.project.exception;

public class PaymentEntityNotFoundException extends RuntimeException {

    public PaymentEntityNotFoundException(Long appointmentId) {
        super("Appointment not found with ID: " + appointmentId);
    }
}
