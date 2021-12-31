package pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.entities;

import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.Roles;

import java.io.Serializable;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Entity
@NamedQueries({@NamedQuery(
        name = "getAllUsers",
        query = "SELECT s FROM User s ORDER BY s.name"
)})
@Table(
        name = "users"
)
@Inheritance(
        strategy = InheritanceType.SINGLE_TABLE
)
public class User implements Serializable {
    @Id
    private String username;
    @NotNull
    private String password;
    @NotNull
    private String name;
    @Email
    @NotNull
    private String email;
    @Version
    private int version;
    @NotNull
    private Roles role;
    @NotNull
    private boolean active;

    public User() {
    }

    public User(String username, String password, String name, String email, int version, Roles role,boolean active) {
        this.username = username;
        this.password = hashPassword(password);
        this.name = name;
        this.email = email;
        this.version = version;
        this.role = role;
        this.active = true;
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

    public int getVersion() {
        return this.version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public static String hashPassword(String password) {
        char[] encoded = null;

        try {
            ByteBuffer passwdBuffer = Charset.defaultCharset().encode(CharBuffer.wrap(password));
            byte[] passwdBytes = passwdBuffer.array();
            MessageDigest mdEnc = MessageDigest.getInstance("SHA-256");
            mdEnc.update(passwdBytes, 0, password.toCharArray().length);
            encoded = (new BigInteger(1, mdEnc.digest())).toString(16).toCharArray();
        } catch (NoSuchAlgorithmException var5) {
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, (String)null, var5);
        }

        return new String(encoded);
    }
}
