package pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.entities;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Table(
        name = "MESURMENTS",
        uniqueConstraints = {@UniqueConstraint(
                columnNames = {"ID"}
        )}
)
@NamedQueries({@NamedQuery(
        name = "getAllMeasurements",
        query = "SELECT m FROM Measurement m ORDER BY m.user.name"
)})
@Entity
public class Measurement {
    @Id
    private int id;
    @ManyToOne
    private User user;
    private String inputSource;
    private String value;
    @ManyToMany(
            mappedBy = "measurements"
    )
    private List<BiometricData> biometrics;

    public Measurement() {
        this.biometrics = new ArrayList();
    }

    public Measurement(User user, String inputSource, String value) {
        this.user = user;
        this.inputSource = inputSource;
        this.value = value;
        this.biometrics = new ArrayList();
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getInputSource() {
        return this.inputSource;
    }

    public void setInputSource(String inputOrigin) {
        this.inputSource = inputOrigin;
    }

    public List<BiometricData> getBiometrics() {
        return this.biometrics;
    }

    public void setBiometrics(List<BiometricData> biometrics) {
        this.biometrics = biometrics;
    }

    public void addBiometrics(BiometricData biometric) {
        this.biometrics.add(biometric);
    }

    public void removeBiometrics(BiometricData biometric) {
        this.biometrics.remove(biometric);
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
