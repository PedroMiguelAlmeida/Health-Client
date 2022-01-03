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

    @ManyToMany(mappedBy = "students")
    private List<Prescription> prescriptions;

    public Measurement() {

    }

    public Measurement(MeasureType measureType, String value, String inputSource, Patient patient, List<Prescription> prescriptions) {
        this.measureType = measureType;
        this.value = value;
        this.inputSource = inputSource;
        this.patient = patient;
        this.prescriptions = prescriptions;
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
}
