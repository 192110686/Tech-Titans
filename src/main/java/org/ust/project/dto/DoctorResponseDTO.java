package org.ust.project.dto;

import lombok.Data;

@Data
public class DoctorResponseDTO {

    private Long id;

    private String firstName;

    private String lastName;

    private String specialization;

    private String availabilitySchedule;

    private Long contactNumber;

    private String email;
}
