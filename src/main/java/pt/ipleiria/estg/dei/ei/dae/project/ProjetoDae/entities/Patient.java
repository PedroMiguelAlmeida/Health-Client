package pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

@NamedQueries({@NamedQuery(
        name = "getAllPatients",
        query = "SELECT s FROM Patient s ORDER BY s.name"
)})
@Entity
public class Patient extends User implements Serializable {
    @OneToMany(
            mappedBy = "user",
            cascade = {CascadeType.REMOVE}
    )
    private List<Measurement> measurements;

    public Patient() {
    }

    public Patient(String username, String password, String name, String email, int version) {
        super(username, password, name, email, version);
        this.measurements = new ArrayList();
    }

    public List<Measurement> getMeasurementsList() {
        return this.measurements;
    }

    public void setMeasurementsList(List<Measurement> measurements) {
        this.measurements = measurements;
    }
}
