package org.ust.project.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class DoctorRequestDTO {

    private String firstName;

    private String lastName;

    private String specialization;

    private Long contactNumber;

    private String email;

    private String licenseNumber;

    private String availabilitySchedule;
}
