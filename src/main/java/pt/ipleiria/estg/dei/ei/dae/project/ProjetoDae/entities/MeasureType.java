
package pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.entities;

import io.smallrye.common.constraint.NotNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;

@Table(
        name = "MEASURETYPE",
        uniqueConstraints = {@UniqueConstraint(
                columnNames = {"NAME", "DTYPE"}
        )}
)
@NamedQueries({@NamedQuery(
        name = "getAllMeasureTypes",
        query = "SELECT a FROM MeasureType a ORDER BY a.id"
)})
@Entity
@Inheritance(
        strategy = InheritanceType.SINGLE_TABLE
)
public abstract class MeasureType implements Serializable {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private int id;
    @NotNull
    private String name;
    @NotNull
    private boolean multiple;

    @OneToMany(mappedBy = "measureType", cascade = CascadeType.REMOVE)
    private List<Measurement> measurements;

    public MeasureType() {
        this.measurements = new ArrayList();
    }

    public MeasureType(String name, boolean multiple) {
        this.name = name;
        this.multiple = multiple;
        this.measurements = new ArrayList();
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isMultiple() {
        return this.multiple;
    }

    public void setMultiple(boolean multiple) {
        this.multiple = multiple;
    }

    public List<Measurement> getMeasurements() {
        return this.measurements;
    }

    public void setMeasurements(List<Measurement> measurements) {
        this.measurements = measurements;
    }

    public void addMeasurement(Measurement measurement) {
        this.measurements.add(measurement);
    }

    public void removeMeasurement(Measurement measurement) {
        this.measurements.remove(measurement);
    }
}