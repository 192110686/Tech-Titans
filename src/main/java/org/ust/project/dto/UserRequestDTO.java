package org.ust.project.dto;

public class UserRequestDTO {

    private String username;

    private String password;

    private String role;
    
    public UserRequestDTO(){}

    public UserRequestDTO(String password, String role, String username) {
        this.password = password;
        this.role = role;
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
        sb.append("UserRequestDTO{");
        sb.append("username=").append(username);
        sb.append(", password=").append(password);
        sb.append(", role=").append(role);
        sb.append('}');
        return sb.toString();
    }
    

}
