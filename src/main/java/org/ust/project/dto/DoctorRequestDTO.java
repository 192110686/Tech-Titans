package org.ust.project.dto;

import lombok.Data;

@Data
public class DoctorRequestDTO {

    private String firstName;

    private String lastName;

    private String specialization;

    private Long contactNumber;

    private String email;

    private String licenseNumber;

    private String availabilitySchedule;
}
