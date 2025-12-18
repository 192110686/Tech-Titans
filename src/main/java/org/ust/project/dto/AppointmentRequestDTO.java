package org.ust.project.dto;

import java.time.LocalDate;


public class AppointmentRequestDTO {

    private LocalDate appointmentDate;

    private String timeSlot;

    private String reasonForVisit;

    private Long doctorId;

    private Long patientId;


    public AppointmentRequestDTO(){}

    public AppointmentRequestDTO(LocalDate appointmentDate, Long doctorId, Long patientId, String reasonForVisit, String timeSlot) {
        this.appointmentDate = appointmentDate;
        this.doctorId = doctorId;
        this.patientId = patientId;
        this.reasonForVisit = reasonForVisit;
        this.timeSlot = timeSlot;
    }

    public LocalDate getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(LocalDate appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public String getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(String timeSlot) {
        this.timeSlot = timeSlot;
    }

    public String getReasonForVisit() {
        return reasonForVisit;
    }

    public void setReasonForVisit(String reasonForVisit) {
        this.reasonForVisit = reasonForVisit;
    }

    public Long getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Long doctorId) {
        this.doctorId = doctorId;
    }

    public Long getPatientId() {
        return patientId;
    }

    public void setPatientId(Long patientId) {
        this.patientId = patientId;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("AppointmentRequestDTO{");
        sb.append("appointmentDate=").append(appointmentDate);
        sb.append(", timeSlot=").append(timeSlot);
        sb.append(", reasonForVisit=").append(reasonForVisit);
        sb.append(", doctorId=").append(doctorId);
        sb.append(", patientId=").append(patientId);
        sb.append('}');
        return sb.toString();
    }


}
