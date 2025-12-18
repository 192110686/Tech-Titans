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
public class DoctorResponseDTO {

    private Long id;

    private String firstName;

    private String lastName;

    private String specialization;

    private String availabilitySchedule;

}
