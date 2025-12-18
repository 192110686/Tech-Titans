package org.ust.project.dto;

public class UserResponseDTO {

    private Long id;

    private String username;

    private String role;

public UserResponseDTO(){}

    public UserResponseDTO(Long id, String role, String username) {
        this.id = id;
        this.role = role;
        this.username = username;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("UserResponseDTO{");
        sb.append("id=").append(id);
        sb.append(", username=").append(username);
        sb.append(", role=").append(role);
        sb.append('}');
        return sb.toString();
    }



}
