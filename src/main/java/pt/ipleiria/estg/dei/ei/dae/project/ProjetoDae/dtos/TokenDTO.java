package pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.dtos;

import java.security.SecureRandom;

public class TokenDTO {
    private String token = generateToken();
    private String email;

    public TokenDTO() {

    }

    public TokenDTO(String token, String email) {
        this.token = token;
        this.email = email;
    }

    public String generateToken(){
        SecureRandom random = new SecureRandom();
        byte bytes[] = new byte[20];
        random.nextBytes(bytes);
        return token = bytes.toString();
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
