package org.ust.project.dto;

import java.time.LocalDateTime;
import java.util.List;

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

     // List of available time slots
    private List<LocalDateTime> availableSchedule;
    
    private Long userId;
}
