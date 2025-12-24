package org.ust.project.model;

      public enum AppointmentStatus {
    SCHEDULED,        // Appointment is scheduled but not started
    IN_PROGRESS,      // Consultation is ongoing
    COMPLETED,        // Consultation has been completed
    CANCELLED,        // Appointment was cancelled
    RESCHEDULED       // Appointment has been rescheduled
}
