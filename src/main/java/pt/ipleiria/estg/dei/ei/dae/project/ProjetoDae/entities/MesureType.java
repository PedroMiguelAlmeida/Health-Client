
package pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.entities;

import io.smallrye.common.constraint.NotNull;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Table(
        name = "BIOMETRIC_DATA",
        uniqueConstraints = {@UniqueConstraint(
                columnNames = {"NAME"}
        )}
)
@NamedQueries({@NamedQuery(
        name = "getAllBiometricData",
        query = "SELECT bd FROM MesureType bd ORDER BY bd.id"
)})
@Entity
public class MesureType {
    @Id
    private int id;
    @NotNull
    private String name;
    @NotNull
    private boolean multiple;
    @ManyToMany
    @JoinTable(
            name = "BIOMETRICDATAS_MEASURMENTS",
            joinColumns = {@JoinColumn(
                    name = "BIOMETRICDATA_ID",
                    referencedColumnName = "ID"
            )},
            inverseJoinColumns = {@JoinColumn(
                    name = "MEASURMENT_ID",
                    referencedColumnName = "ID"
            )}
    )
    private List<Measurement> measurements;

    public MesureType() {
        this.measurements = new ArrayList();
    }

    public MesureType(int id, String name, boolean multiple, List<Measurement> measurements) {
        this.id = id;
        this.name = name;
        this.multiple = multiple;
        this.measurements = new ArrayList();
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
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
