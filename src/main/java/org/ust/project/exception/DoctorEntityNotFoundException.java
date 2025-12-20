package org.ust.project.exception;

public class DoctorEntityNotFoundException extends RuntimeException {

    public DoctorEntityNotFoundException(Long doctorId) {
        super(String.format("Doctor with ID %d not found", doctorId));
    }
}
