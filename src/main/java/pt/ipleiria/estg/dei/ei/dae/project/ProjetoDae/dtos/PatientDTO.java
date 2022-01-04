package pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.dtos;

import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.Roles;
import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.entities.HealthProfessional;

import java.util.ArrayList;
import java.util.List;

public class PatientDTO {
    private String username;
    private String password;
    private String name;
    private String email;
    private Roles role;
    private boolean active;
    private List<HealthProfessionalDTO> healthProfessionalDTOList;

    public PatientDTO() {
    }

    public PatientDTO(String username, String password, String name, String email, Roles role, boolean active, List<HealthProfessional> healthProfessionals) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.email = email;
        this.role = role;
        this.active = active;
        this.healthProfessionalDTOList = new ArrayList<>();
    }

    public List<HealthProfessionalDTO> getHealthProfessionalDTOList() {
        return healthProfessionalDTOList;
    }

    public void setHealthProfessionalDTOList(List<HealthProfessionalDTO> healthProfessionalDTOList) {
        this.healthProfessionalDTOList = healthProfessionalDTOList;
    }

    public Roles getRole() {
        return role;
    }

    public void setRole(Roles role) {
        this.role = role;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
