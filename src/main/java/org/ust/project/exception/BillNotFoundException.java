package org.ust.project.exception;

public class BillNotFoundException extends RuntimeException {

    public BillNotFoundException(Long billId) {
        super("Bill not found with ID: " + billId);
    }
}
