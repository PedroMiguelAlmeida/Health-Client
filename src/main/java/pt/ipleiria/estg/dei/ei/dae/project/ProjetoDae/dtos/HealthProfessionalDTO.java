package pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.dtos;

import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.Roles;

public class HealthProfessionalDTO {
    private String username;
    private String password;
    private String name;
    private String email;
    private int  version;
    private String profession;
    private boolean chefe;
    private Roles role;

    public HealthProfessionalDTO(){}

    public HealthProfessionalDTO(String username, String password, String name, String email, int version, String profession, boolean chefe, Roles role){
        this.username = username;
        this.password = password;
        this.name = name;
        this.email = email;
        this.version = version;
        this.profession = profession;
        this.chefe = chefe;
        this.role = role;
    }

    public Roles getRole() {
        return role;
    }

    public void setRole(Roles role) {
        this.role = role;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public boolean isChefe() {
        return chefe;
    }

    public void setChefe(boolean chefe) {
        this.chefe = chefe;
    }
}