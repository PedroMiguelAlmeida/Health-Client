package pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.dtos;

import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.Roles;
import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.entities.Administrator;

public class AdministratorDTO {
    private String username;
    private String password;
    private String name;
    private String email;
    private int version;
    private Roles role;

    public AdministratorDTO(){
    }

    public AdministratorDTO(String username,String password,String name,String email,int version,Roles role){
        this.username=username;
        this.password=password;
        this.name=name;
        this.email=email;
        this.version=version;
        this.role=role;
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

    public Roles getRole() {
        return role;
    }

    public void setRole(Roles role) {
        this.role = role;
    }
}
