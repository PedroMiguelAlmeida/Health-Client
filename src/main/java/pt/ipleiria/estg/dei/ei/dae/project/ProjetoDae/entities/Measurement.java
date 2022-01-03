package pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.entities;

import io.smallrye.common.constraint.NotNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;

@Table(
        name = "MEASURMENTS",
        uniqueConstraints = {@UniqueConstraint(
                columnNames = {"ID"}
        )}
)
@NamedQueries({@NamedQuery(
        name = "getAllMeasurements",
        query = "SELECT m FROM Measurement m ORDER BY m.patient.name"
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

    public Measurement() {

    }

    public Measurement(MeasureType measureType, String value, String inputSource, Patient patient) {
        this.measureType = measureType;
        this.value = value;
        this.inputSource = inputSource;
        this.patient = patient;
    }

    public int getId() {
        return id;
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

    public User getUser() {
        return patient;
    }

    public void setUser(User user) {
        this.patient = patient;
    }
}
