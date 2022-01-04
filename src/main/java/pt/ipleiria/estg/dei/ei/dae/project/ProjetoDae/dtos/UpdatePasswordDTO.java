package pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.dtos;

import java.security.SecureRandom;

public class UpdatePasswordDTO {
    private String token;
    private String password;

    public UpdatePasswordDTO() {

    }

    public UpdatePasswordDTO(String token, String password) {
        this.token = token;
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
