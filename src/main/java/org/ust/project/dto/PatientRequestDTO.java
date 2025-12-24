package org.ust.project.dto;

import java.time.LocalDate;

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
public class PatientRequestDTO {

    private String firstName;

    private String lastName;

    private LocalDate dateOfBirth;

    private String gender;

    private Long phoneNumber;

    private String email;

    private String bloodGroup;
    
    private String address;
    
    private Long userId;
    
}
