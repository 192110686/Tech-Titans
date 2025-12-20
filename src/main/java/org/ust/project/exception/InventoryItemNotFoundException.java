package org.ust.project.exception;

public class InventoryItemNotFoundException extends RuntimeException {

    public InventoryItemNotFoundException(Long inventoryItemId) {
        super("Appointment not found with ID: " + inventoryItemId);
    }
}
