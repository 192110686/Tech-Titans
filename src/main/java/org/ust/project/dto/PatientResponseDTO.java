package org.ust.project.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;


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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Long getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(Long phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    // public PatientResponseDTO(LocalDate dateOfBirth, String firstName, Long id, String lastName, Long phoneNumber) {
    //     this.dateOfBirth = dateOfBirth;
    //     this.firstName = firstName;
    //     this.id = id;
    //     this.lastName = lastName;
    //     this.phoneNumber = phoneNumber;
    // }


}
