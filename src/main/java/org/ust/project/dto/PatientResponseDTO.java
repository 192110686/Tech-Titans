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
public class PatientResponseDTO {

    private Long id;

    private String firstName;

    private String lastName;

    private LocalDate dateOfBirth;

    private String gender;

    private Long phoneNumber;

    private String email;

    private String bloodGroup;

    public PatientResponseDTO(LocalDate dateOfBirth, String firstName, Long id, String lastName, Long phoneNumber) {
        this.dateOfBirth = dateOfBirth;
        this.firstName = firstName;
        this.id = id;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
    }


}
