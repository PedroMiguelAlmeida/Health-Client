package pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.entities;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;

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
    @ManyToOne
    @JoinColumn(name = "MEASURETYPE_ID")
    private MeasureType measureType;

    public Measurement() {

    }

    public Measurement(User user, String inputSource, String value, MeasureType measureType) {
        this.user = user;
        this.inputSource = inputSource;
        this.value = value;
        this.measureType = measureType;
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

    public void setInputSource(String inputSource) {
        this.inputSource = Measurement.this.inputSource;
    }

    public MeasureType getMeasureType() {
        return measureType;
    }

    public void setMeasureType(MeasureType measureType) {
        this.measureType = measureType;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
