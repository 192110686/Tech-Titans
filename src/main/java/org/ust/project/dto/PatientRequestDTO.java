package org.ust.project.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class PatientRequestDTO {

    private String firstName;

    private String lastName;

    private LocalDate dateOfBirth;

    private String gender;

    private Long phoneNumber;

    private String email;

    private String bloodGroup;
}
