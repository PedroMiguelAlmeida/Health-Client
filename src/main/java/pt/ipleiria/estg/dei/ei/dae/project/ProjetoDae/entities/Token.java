package pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.entities;



import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.security.SecureRandom;

@Table(name = "TOKENS")
@Entity
public class Token implements Serializable {
    @NotNull
    String token = generateToken();
    @Id
    String email;

    public Token() {

    }

    public Token(String email) {
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
