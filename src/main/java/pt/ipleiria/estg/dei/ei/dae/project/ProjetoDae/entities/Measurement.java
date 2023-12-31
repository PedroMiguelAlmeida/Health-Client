package pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.entities;

import io.smallrye.common.constraint.NotNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;

@Table(
        name = "MEASUREMENT"
)
@NamedQueries({@NamedQuery(
        name = "getAllMeasurements",
        query = "SELECT m FROM Measurement m"
)})
@Entity
public class Measurement implements Serializable {
    @Id
    @GeneratedValue
    private int id;

    @ManyToOne
    @JoinColumn(name = "MEASURETYPE_ID")
    private MeasureType measureType;

    @NotNull
    private String value;

    private String inputSource;

    @ManyToOne
    private Patient patient;

    @ManyToMany(mappedBy = "measurements")
    private List<Prescription> prescriptions;

    @Version
    private int version;

    @NotNull
    private boolean active;

    public Measurement() {
        this.prescriptions = new ArrayList<>();
    }

    public Measurement(MeasureType measureType, String value, String inputSource, Patient patient, int version, boolean active) {
        this.measureType = measureType;
        this.value = value;
        this.inputSource = inputSource;
        this.patient = patient;
        this.prescriptions = new ArrayList<>();
        this.version = version;
        this.active = active;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public MeasureType getMeasureType() {
        return measureType;
    }

    public void setMeasureType(MeasureType measureType) {
        this.measureType = measureType;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getInputSource() {
        return inputSource;
    }

    public void setInputSource(String inputSource) {
        this.inputSource = inputSource;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public List<Prescription> getPrescriptions() {
        return prescriptions;
    }

    public void setPrescriptions(List<Prescription> prescriptions) {
        this.prescriptions = prescriptions;
    }

    public void addPrescription(Prescription prescription) {
        this.prescriptions.add(prescription);
    }

    public void removePrescription(Prescription prescription) {
        this.prescriptions.remove(prescription);
    }

    public int getVersion() {
        return version;
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
}
