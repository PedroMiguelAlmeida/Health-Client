package pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.entities;

import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.Roles;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;

@NamedQueries({@NamedQuery(
        name = "getAllPatients",
        query = "SELECT s FROM Patient s ORDER BY s.name"
)})
@Entity
public class Patient extends User implements Serializable {
    @OneToMany(
            mappedBy = "patient",
            cascade = {CascadeType.REMOVE}
    )
    private List<Measurement> measurements;

    public Patient() {
    }

    @ManyToMany (mappedBy = "patients")
    List<HealthProfessional> healthProfessionals;

    public Patient(String username, String password, String name, String email, int version, Roles role,boolean active) {
        super(username, password, name, email, version, role,active);
        this.measurements = new ArrayList();
        this.healthProfessionals = new ArrayList();
    }

    public List<Measurement> getMeasurementsList() {
        return this.measurements;
    }

    public void setMeasurementsList(List<Measurement> measurements) {
        this.measurements = measurements;
    }


    public List<HealthProfessional> getHealthProfessionals() {
        return healthProfessionals;
    }

    public void setHealthProfessionals(List<HealthProfessional> healthProfessionals) {
        this.healthProfessionals = healthProfessionals;
    }

    public void addHealthProfessional( HealthProfessional healthProfessional) {
        for (HealthProfessional healthProfessionali:healthProfessionals) {
            if (healthProfessional.equals(healthProfessionali)){
                System.out.println("This patient is already on the list");
                return;
            }
        }
        if (!healthProfessionals.add(healthProfessional)){
            System.out.println("ERRO no addHealthProfessional patient");
        }else {
            System.out.println("Adcionado com sucesso");
        }

    }

    public void removeHealthProfessionals(HealthProfessional healthProfessional){healthProfessionals.removeIf(healthProfessional::equals);}
}
