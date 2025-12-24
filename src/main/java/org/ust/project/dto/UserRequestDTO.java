package org.ust.project.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Setter
public class UserRequestDTO {

    private String username;

    private String password;
    
    private String role;
    
    private Long patientId;

}
